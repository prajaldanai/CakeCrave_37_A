package com.example.cakecrave.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.cakecrave.viewmodel.ProfileViewModel

@Composable
fun DashboardHeader(
    profileVM: ProfileViewModel
) {
    val profile by profileVM.profile.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ✅ Profile Image   profile image is added
        AsyncImage(
            model = profile.photoUrl.takeIf { it.isNotBlank() },
            contentDescription = "Profile Photo",
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color(0xFFF1F1F1))
        )

        Spacer(modifier = Modifier.width(14.dp))

        // ✅ Greeting Text green text added
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = if (profile.name.isNotBlank())
                    "Hey, ${profile.name}"
                else
                    "Hey, User",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2B2B2B)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Find and get your favorite cake",
                fontSize = 14.sp,
                color = Color(0xFF8A8A8A)
            )
        }
    }
}
