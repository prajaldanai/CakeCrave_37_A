package com.example.cakecrave.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt

import androidx.compose.material.icons.filled.CameraAlt

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cakecrave.model.UserProfile
import com.example.cakecrave.viewmodel.ProfileViewModel

@Composable
fun AccountScreen(
    profileViewModel: ProfileViewModel,
    onBack: (() -> Unit)? = null // optional (if you want back arrow)
) {
    val p = profileViewModel.profile

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    LaunchedEffect(p) {
        name = p.name
        phone = p.phone
        dob = p.dob
        country = p.country
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) profileViewModel.updatePhoto(uri)
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
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Top row (back button like your image)
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
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    } else {
                        Spacer(modifier = Modifier.size(42.dp))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "Edit Profile",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.size(42.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ✅ Photo circle + camera icon (same style)
                Box(contentAlignment = Alignment.BottomEnd) {
                    AsyncImage(
                        model = if (p.photoUrl.isNotBlank()) p.photoUrl else null,
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color(0xFFEAEAEA), CircleShape)
                            .background(Color(0xFFEFEFEF)),
                    )

                    IconButton(
                        onClick = { imagePicker.launch("image/*") },
                        modifier = Modifier
                            .offset(x = (-6).dp, y = (-6).dp)
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF3D3B70))
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = "Change photo",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                ProfileField(label = "Name", value = name, onChange = { name = it })
                ProfileField(label = "Contact Number", value = phone, onChange = { phone = it })
                ProfileField(label = "Date of Birth", value = dob, onChange = { dob = it })
                ProfileField(label = "Country/Region", value = country, onChange = { country = it })

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        profileViewModel.saveProfile(
                            UserProfile(
                                name = name,
                                phone = phone,
                                dob = dob,
                                country = country,
                                photoUrl = p.photoUrl
                            )
                        )
                    },
                    modifier = Modifier
                        .padding(horizontal = 40.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A00))
                ) {
                    Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                if (profileViewModel.message.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(profileViewModel.message, fontSize = 12.sp)
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
