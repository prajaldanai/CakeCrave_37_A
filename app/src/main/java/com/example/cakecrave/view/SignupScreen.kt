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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
    onLoginClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    // ✅ Observe signup/login success  login deatrures
    val loginSuccess by viewModel.loginSuccess.collectAsState()

    // 🔥 SAFE NAVIGATION
    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
            onSignupSuccess()
            viewModel.resetLoginState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // ================= TITLE  main tile of the  =================
            Column(
                modifier = Modifier.padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CakeCrave",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = ChocolateBrown
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sign Up",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = ChocolateBrown
                )
            }

            // ================= FORM =================
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                InputField("Full name", name, { name = it }, Modifier.testTag("name"))
                Spacer(modifier = Modifier.height(12.dp))
                InputField("Email address", email, { email = it }, Modifier.testTag("regEmail"))
                Spacer(modifier = Modifier.height(12.dp))
                PasswordField("Password", password, { password = it }, Modifier.testTag("regPassword"))
                Spacer(modifier = Modifier.height(12.dp))
                PasswordField("Confirm password", confirmPassword, { confirmPassword = it })

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        when {
                            name.isBlank() || email.isBlank() ||
                                    password.isBlank() || confirmPassword.isBlank() -> {
                                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                            }

                            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                                Toast.makeText(context, "Enter a valid email address", Toast.LENGTH_SHORT).show()
                            }

                            password.length < 6 -> {
                                Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                            }

                            password != confirmPassword -> {
                                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                            }

                            else -> {
                                viewModel.signup(
                                    name = name,
                                    email = email,
                                    password = password,
                                    confirmPassword = confirmPassword,
                                    onError = { error ->
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("createAccountBtn"),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeBtn)
                ) {
                    Text(
                        text = "Create account",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row {
                    Text("Already have an account? ", color = ChocolateBrown)
                    Text(
                        text = "Login",
                        color = ChocolateBrown,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onLoginClick() }
                    )
                }
            }
        }
    }
}

/* ----------------- REUSABLE FIELDS ----------------- */

@Composable
private fun InputField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = HintBrown) },
        singleLine = true,
        modifier = modifier
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

@Composable
private fun PasswordField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = HintBrown) },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        modifier = modifier
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
