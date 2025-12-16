package com.example.cakecrave.view

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cakecrave.R
import com.example.cakecrave.ui.theme.*
import com.example.cakecrave.viewmodel.AuthViewModel

@Composable
fun SignupScreen(
    viewModel: AuthViewModel,
    onSignupSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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

        // 🔹 Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            WarmOverlay.copy(alpha = 0.35f),
                            WarmOverlay.copy(alpha = 0.15f),
                            WarmOverlay.copy(alpha = 0.35f)
                        )
                    )
                )
        )

        // 🔹 CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // ================= TITLE =================
            Column(
                modifier = Modifier.padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CakeCrave",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ChocolateBrown
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Sign Up",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = ChocolateBrown
                )
            }

            // ================= FORM =================
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                SignupField("Name", name) { name = it }
                Spacer(modifier = Modifier.height(14.dp))

                SignupField("Email address", email) { email = it }
                Spacer(modifier = Modifier.height(14.dp))

                SignupField("Password", password, true) { password = it }
                Spacer(modifier = Modifier.height(14.dp))

                SignupField("Confirm Password", confirmPassword, true) {
                    confirmPassword = it
                }

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    enabled = !isLoading,
                    onClick = {
                        if (isLoading) return@Button
                        isLoading = true

                        when {
                            name.isBlank() || email.isBlank() ||
                                    password.isBlank() || confirmPassword.isBlank() -> {
                                toast(context, "All fields are required")
                                isLoading = false
                            }

                            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                                toast(context, "Enter a valid email address")
                                isLoading = false
                            }

                            password.length < 6 -> {
                                toast(context, "Password must be at least 6 characters")
                                isLoading = false
                            }

                            password != confirmPassword -> {
                                toast(context, "Passwords do not match")
                                isLoading = false
                            }

                            else -> {
                                viewModel.signup(
                                    name = name,              // ✅ FIXED
                                    email = email,
                                    password = password,
                                    confirmPassword = confirmPassword,
                                    onSuccess = {
                                        toast(
                                            context,
                                            "Account created successfully. Please log in."
                                        )

                                        // clear fields
                                        name = ""
                                        email = ""
                                        password = ""
                                        confirmPassword = ""

                                        isLoading = false
                                        onSignupSuccess()
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
                            text = "Create Account",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                Row {
                    Text(
                        text = "Already have an account? ",
                        color = Color.White
                    )
                    Text(
                        text = "Log in",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onLoginClick() }
                    )
                }
            }
        }
    }
}

@Composable
private fun SignupField(
    label: String,
    value: String,
    isPassword: Boolean = false,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text(label, color = HintBrown) },
        singleLine = true,
        visualTransformation =
            if (isPassword) PasswordVisualTransformation()
            else VisualTransformation.None,
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
}

private fun toast(context: android.content.Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}
