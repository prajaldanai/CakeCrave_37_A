package com.example.cakecrave.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cakecrave.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database =
        FirebaseDatabase.getInstance().reference.child("profiles")

    fun getUserProfile(onResult: (UserProfile?) -> Unit) {
        val uid = auth.currentUser?.uid ?: run {
            onResult(null)
            return
        }

        database.child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val profile = snapshot.getValue(UserProfile::class.java)
                    onResult(profile)
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(null)
                }
            })
    }

    fun saveUserProfile(
        profile: UserProfile,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: run {
            onError("User not logged in")
            return
        }

        database.child(uid)
            .setValue(profile)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                onError(it.message ?: "Profile update failed")
            }
    }
}
