package com.example.cakecrave.view

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.cakecrave.model.UserProfile
import com.example.cakecrave.utils.ImageUtils
import com.example.cakecrave.viewmodel.ProfileViewModel

@Composable
fun AccountScreen(
    onBack: (() -> Unit)?,
    profileViewModel: ProfileViewModel
) {
    val vm = profileViewModel

    // 🔥 Load profile safely
    LaunchedEffect(Unit) {
        vm.loadProfile()
    }

    val profile by vm.profile.collectAsState()
    val message by vm.message.collectAsState()

    val context = LocalContext.current
    val activity = context as ComponentActivity

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var photoUrl by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var uploading by remember { mutableStateOf(false) }

    // Sync UI fields when profile changes
    LaunchedEffect(profile) {
        name = profile.name
        phone = profile.phone
        dob = profile.dob
        country = profile.country
        photoUrl = profile.photoUrl
    }

    // 🔥 Image picker (same utility as products)
    val imageUtils = remember {
        ImageUtils(activity, activity)
    }

    LaunchedEffect(Unit) {
        imageUtils.registerLaunchers { uri ->
            selectedImageUri = uri
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1F1F1F)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ================= HEADER =================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onBack != null) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF3F3F3))
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(42.dp))
                    }

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = "Edit Profile",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.weight(1f))
                    Spacer(modifier = Modifier.size(42.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ================= PROFILE IMAGE =================
                AsyncImage(
                    model = selectedImageUri ?: photoUrl.takeIf { it.isNotBlank() },
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFFEAEAEA), CircleShape)
                        .background(Color(0xFFEFEFEF))
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { imageUtils.launchImagePicker() }
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Change Profile Photo")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ================= FIELDS =================
                ProfileField("Name", name) { name = it }
                ProfileField("Contact Number", phone) { phone = it }
                ProfileField("Date of Birth", dob) { dob = it }
                ProfileField("Country/Region", country) { country = it }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        // If image changed → upload first
                        if (selectedImageUri != null) {
                            uploading = true
                            vm.uploadProfileImage(context, selectedImageUri) { url ->
                                uploading = false
                                if (!url.isNullOrBlank()) {
                                    photoUrl = url
                                    vm.saveProfile(
                                        UserProfile(
                                            name = name,
                                            phone = phone,
                                            dob = dob,
                                            country = country,
                                            photoUrl = url
                                        )
                                    )
                                }
                            }
                        } else {
                            // No image change
                            vm.saveProfile(
                                UserProfile(
                                    name = name,
                                    phone = phone,
                                    dob = dob,
                                    country = country,
                                    photoUrl = photoUrl
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(50),
                    enabled = !uploading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A00))
                ) {
                    Text(
                        if (uploading) "Saving..." else "Save Changes",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (message.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(message, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}
