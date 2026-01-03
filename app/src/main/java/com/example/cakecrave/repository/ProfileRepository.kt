package com.example.cakecrave.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.cakecrave.model.UserProfile
import com.google.firebase.database.*
import java.io.InputStream
import java.util.concurrent.Executors

class ProfileRepository {

    // ================= FIREBASE =================
    private val db =
        FirebaseDatabase.getInstance()
            .reference
            .child("profiles")

    // ================= CLOUDINARY =================
    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dcevekywh",
            "api_key" to "829685644631527",
            "api_secret" to "i_zTrr8kRBPzsMs-wX2PDygOPHk"
        )
    )

    // ================= OBSERVE PROFILE =================
    fun observeProfile(
        userId: String,
        onResult: (UserProfile) -> Unit,
        onError: (String) -> Unit
    ) {
        db.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val profile =
                        snapshot.getValue(UserProfile::class.java)
                            ?: UserProfile()
                    onResult(profile)
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(error.message)
                }
            })
    }

    // ================= SAVE PROFILE =================
    fun saveProfile(
        userId: String,
        profile: UserProfile,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        db.child(userId)
            .setValue(profile)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                onError(it.message ?: "Failed to save profile")
            }
    }

    // ================= UPLOAD PROFILE IMAGE =================
    fun uploadProfileImage(
        context: Context,
        imageUri: Uri,
        callback: (String?) -> Unit
    ) {
        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            try {
                val inputStream: InputStream? =
                    context.contentResolver.openInputStream(imageUri)

                if (inputStream == null) {
                    Handler(Looper.getMainLooper()).post { callback(null) }
                    return@execute
                }

                val fileName =
                    getFileNameFromUri(context, imageUri)?.substringBeforeLast(".")
                        ?: "profile_image"

                val response = cloudinary.uploader().upload(
                    inputStream,
                    ObjectUtils.asMap(
                        "public_id", "profiles/$fileName",
                        "resource_type", "image"
                    )
                )

                var imageUrl = response["url"] as String?
                imageUrl = imageUrl?.replace("http://", "https://")

                Handler(Looper.getMainLooper()).post {
                    callback(imageUrl)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    callback(null)
                }
            }
        }
    }

    // ================= HELPER =================
    private fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor: Cursor? =
            context.contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    fileName = it.getString(index)
                }
            }
        }
        return fileName
    }
}
