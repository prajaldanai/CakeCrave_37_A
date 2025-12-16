package com.example.cakecrave.repository

import android.net.Uri
import com.example.cakecrave.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    private fun uid(): String = auth.currentUser?.uid ?: ""

    private fun userRef(): DatabaseReference =
        database.getReference("users").child(uid())

    fun observeProfile(
        onResult: (UserProfile) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        if (uid().isBlank()) {
            onError("User not logged in")
            return
        }

        userRef().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profile =
                    snapshot.getValue(UserProfile::class.java) ?: UserProfile()
                onResult(profile)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        })
    }

    fun saveProfile(
        profile: UserProfile,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (uid().isBlank()) {
            onError("User not logged in")
            return
        }

        userRef()
            .setValue(profile)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                onError(it.message ?: "Failed to save profile")
            }
    }

    fun uploadProfilePhoto(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = uid()
        if (userId.isBlank()) {
            onError("User not logged in")
            return
        }

        val photoRef: StorageReference =
            storage.reference.child("profile_photos/$userId.jpg")

        photoRef.putFile(imageUri)
            .addOnSuccessListener {
                photoRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        onSuccess(uri.toString())
                    }
                    .addOnFailureListener {
                        onError(it.message ?: "Failed to get download URL")
                    }
            }
            .addOnFailureListener {
                onError(it.message ?: "Photo upload failed")
            }
    }
}
