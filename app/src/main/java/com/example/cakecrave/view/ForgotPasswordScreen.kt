package com.example.cakecrave.view

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cakecrave.R
import com.example.cakecrave.ui.theme.*
import com.example.cakecrave.viewmodel.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onEmailSent: () -> Unit,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔹 Background Image
        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )

        // 🔹 Warm Overlay (stronger for readability)
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
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                color = ChocolateBrown
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ===== Screen Title =====
            Text(
                text = "Forgot Password",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = ChocolateBrown
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ===== MESSAGE CARD (KEY FIX) =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF3E2723).copy(alpha = 0.78f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Text(
                    text = "Enter your registered email address.\n\n" +
                            "We’ll send you a link to reset your password.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Serif,
                    color = PureWhite,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ===== Email Field =====
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email address", color = HintBrown) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = ChocolateBrown,
                    unfocusedTextColor = ChocolateBrown
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ===== Send Reset Link Button =====
            Button(
                enabled = !isLoading,
                onClick = {
                    if (isLoading) return@Button
                    isLoading = true

                    when {
                        email.isBlank() -> {
                            toast(context, "Email is required")
                            isLoading = false
                        }

                        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            toast(context, "Enter a valid email address")
                            isLoading = false
                        }

                        else -> {
                            viewModel.sendResetLink(
                                email = email,
                                onSuccess = {
                                    toast(
                                        context,
                                        "Reset link sent. Please check your email."
                                    )
                                    isLoading = false
                                    onEmailSent()
                                },
                                onError = {
                                    toast(context, it)
                                    isLoading = false
                                }
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangeBtn)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text(
                        text = "Send Reset Link",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ===== Back to Login BUTTON (styled same) =====
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangeBtn)
            ) {
                Text(
                    text = "Back to Login",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )
            }
        }
    }
}

private fun toast(context: android.content.Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}
