package com.example.cakecrave.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cakecrave.R
import com.example.cakecrave.ui.theme.*

@Composable
fun ResetPasswordScreen(
    onBackToLogin: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 🔹 Background Image
        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )

        // 🔹 Warm Overlay (slightly stronger)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            WarmOverlay.copy(alpha = 0.45f),
                            WarmOverlay.copy(alpha = 0.25f),
                            WarmOverlay.copy(alpha = 0.45f)
                        )
                    )
                )
        )

        // 🔹 CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ===== App Name =====
            Text(
                text = "CakeCrave",
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                color = PureWhite
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ===== Screen Title =====
            Text(
                text = "Check Your Email",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = PureWhite
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ===== MESSAGE CARD (KEY FIX) =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF3E2723).copy(alpha = 0.78f), // dark chocolate glass
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Text(
                    text = "We’ve sent a password reset link to your email address.\n\n" +
                            "Please check your inbox (or spam folder) and follow the instructions to reset your password.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Serif,
                    color = PureWhite,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // ===== Button =====
            Button(
                onClick = onBackToLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangeBtn)
            ) {
                Text(
                    text = "Back to Login",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
