package com.example.cakecrave.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.cakecrave.model.UserProfile
import com.example.cakecrave.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository()
) : ViewModel() {

    private val _profile = MutableStateFlow(UserProfile())
    val profile: StateFlow<UserProfile> = _profile

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    /**
     * 🔥 Call this from UI (LaunchedEffect)
     */
    fun loadProfile() {
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            _message.value = "User not logged in"
            return
        }

        repository.observeProfile(
            userId = user.uid,
            onResult = { _profile.value = it },
            onError = { _message.value = it }
        )
    }

    /**
     * 🔥 Upload profile image to Cloudinary
     * Returns image URL
     */
    fun uploadProfileImage(
        context: Context,
        imageUri: Uri?,
        onResult: (String?) -> Unit
    ) {
        if (imageUri == null) {
            onResult(null)
            return
        }

        repository.uploadProfileImage(context, imageUri) { imageUrl ->
            onResult(imageUrl)
        }
    }

    /**
     * 🔥 Save profile data (including image URL)
     */
    fun saveProfile(profile: UserProfile) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            _message.value = "User not logged in"
            return
        }

        repository.saveProfile(
            userId = user.uid,
            profile = profile,
            onSuccess = {
                _profile.value = profile
                _message.value = "Profile updated"
            },
            onError = { _message.value = it }
        )
    }
}
