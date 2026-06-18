package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.AppViewModel

@Composable
fun LoginScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val authError by viewModel.authError.collectAsState()
    val regSuccess by viewModel.regSuccess.collectAsState()

    var isRegisterMode by remember { mutableStateOf(false) }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Clear errors when toggling modes
    LaunchedEffect(isRegisterMode) {
        viewModel.clearErrors()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A), // Slate 900
                        Color(0xFF1E293B), // Slate 800
                        Color(0xFF0F172A)  // Slate 900
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Hero Space Icon Accent
            Surface(
                modifier = Modifier
                    .size(80.dp),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f),
                tonalElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Launch,
                        contentDescription = "WebToApp Brand Logo",
                        tint = Color(0xFF22D3EE), // Cyan 400
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // App Name Display
            Text(
                text = "WebToApp Studio",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "যেকোনো ওয়েবসাইটকে রূপান্তর করুন অ্যান্ড্রয়েড অ্যাপে",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF94A3B8), // Slate 400
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            // Auth Error Panel
            if (authError != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f),
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "এরর",
                            tint = Color.Red,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = authError ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // Registration Success Alert
            if (regSuccess) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF15803D).copy(alpha = 0.2f),
                        contentColor = Color(0xFF86EFAC)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "সফল",
                            tint = Color(0xFF4ADE80),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "রেজিস্ট্রেশন সফল হয়েছে! লগইন করুন।",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // Main Credential Entry Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E293B).copy(alpha = 0.7f)
                ),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (isRegisterMode) "অ্যাকাউন্ট রেজিস্ট্রেশন" else "ইউজার লগইন",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )

                    // Full Name (Only for Registration)
                    AnimatedVisibility(visible = isRegisterMode) {
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("পূর্ণ নাম (Full Name)", color = Color(0xFF94A3B8)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Person",
                                    tint = Color(0xFF22D3EE)
                                )
                            },
                            textStyle = LocalTextStyle.current.copy(color = Color.White),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_fullname_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF22D3EE),
                                unfocusedBorderColor = Color(0xFF475569)
                            )
                        )
                    }

                    // Username Input
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("মোবাইল নম্বর / ইউজারনেম", color = Color(0xFF94A3B8)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.AccountBox,
                                contentDescription = "Username",
                                tint = Color(0xFF22D3EE)
                            )
                        },
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("username_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF22D3EE),
                            unfocusedBorderColor = Color(0xFF475569)
                        )
                    )

                    // Password Input
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("গোপন পাসওয়ার্ড", color = Color(0xFF94A3B8)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password",
                                tint = Color(0xFF22D3EE)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password visibility",
                                    tint = Color(0xFF94A3B8)
                                )
                            }
                        },
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        singleLine = true,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("password_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF22D3EE),
                            unfocusedBorderColor = Color(0xFF475569)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Action Submit Button
                    Button(
                        onClick = {
                            if (isRegisterMode) {
                                viewModel.register(username, fullName, password)
                                if (regSuccess) {
                                    isRegisterMode = false
                                }
                            } else {
                                viewModel.login(username, password)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF06B6D4), // Cyan 500
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("auth_submit_button")
                    ) {
                        Text(
                            text = if (isRegisterMode) "নিবন্ধন সম্পন্ন করুন" else "অ্যাকাউন্টে প্রবেশ করুন",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Switch Auth Mode Link
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isRegisterMode) "ইতিমধ্যেই অ্যাকাউন্ট আছে? " else "নতুন অ্যাকাউন্ট তৈরি করতে চান? ",
                    color = Color(0xFF94A3B8),
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(
                    onClick = {
                        isRegisterMode = !isRegisterMode
                    },
                    modifier = Modifier.testTag("toggle_auth_mode_button")
                ) {
                    Text(
                        text = if (isRegisterMode) "লগইন করুন" else "রেজিস্ট্রেশন করুন",
                        color = Color(0xFF22D3EE),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
