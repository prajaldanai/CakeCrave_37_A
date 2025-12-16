package com.example.cakecrave.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun DashboardHeader(
    userName: String,
    photoUrl: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = if (photoUrl.isNotBlank()) photoUrl else null,
            contentDescription = "Profile",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color(0xFFEFEFEF))
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = "HEY, ${userName.uppercase()}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Find and Get Your Favorite Cake", fontSize = 14.sp)
        }
    }
}
