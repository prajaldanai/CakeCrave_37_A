package com.example.cakecrave.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cakecrave.R
import com.example.cakecrave.ui.theme.BackgroundDark
import com.example.cakecrave.ui.theme.OrangeBtn
import com.example.cakecrave.ui.theme.PureWhite

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
    ) {

        // Rounded main container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(34.dp))
        ) {

            // Background image
            Image(
                painter = painterResource(id = R.drawable.cake_bg),
                contentDescription = "Cake Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient overlay (lighter, more elegant)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.35f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.50f)
                            )
                        )
                    )
            )

            // Foreground content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 26.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ===== Small welcome =====
                Text(
                    text = "Welcome",
                    color = PureWhite.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(36.dp))

                // ===== Glass Card (PERFECTED) =====
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF3E2723).copy(alpha = 0.70f), // lighter than before
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 22.dp, vertical = 20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        // App name
                        Text(
                            text = "CakeCrave",
                            fontSize = 38.sp, // slightly smaller
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Serif,
                            color = PureWhite
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Tagline
                        Text(
                            text = "Freshly Baked Happiness",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = PureWhite.copy(alpha = 0.95f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Description (lighter + calmer)
                        Text(
                            text = "Order handcrafted cakes made with love,\n" +
                                    "perfect for every celebration and craving.",
                            fontSize = 15.sp,
                            fontFamily = FontFamily.Serif,
                            color = PureWhite.copy(alpha = 0.88f),
                            textAlign = TextAlign.Center,
                            lineHeight = 21.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // ===== CTA Button =====
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 42.dp)
                        .testTag("getStartedBtn"),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeBtn)
                ) {
                    Text(
                        text = "Get Started →",
                        color = PureWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
