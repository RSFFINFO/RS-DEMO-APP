package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.WebApp
import com.example.ui.viewmodel.AppViewModel

@Composable
fun DashboardScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val webApps by viewModel.webApps.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }

    // Navigation and Edge-to-Edge scaffolding
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                // Top Custom Command Bar
                Surface(
                    color = Color(0xFF0F172A),
                    tonalElevation = 4.dp
                ) {
                    val padding = WindowInsets.statusBars.asPaddingValues()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(padding)
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FilterFrames,
                                contentDescription = "Logo",
                                tint = Color(0xFF22D3EE),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "WebToApp Studio",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 19.sp
                                ),
                                color = Color.White
                            )
                        }

                        // Top header Quick App-Create shortcut button
                        IconButton(
                            onClick = { showCreateDialog = true },
                            modifier = Modifier
                                .background(Color(0xFF22D3EE), CircleShape)
                                .size(36.dp)
                                .testTag("top_create_button_icon")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "অ্যাপ তৈরি করুন",
                                tint = Color.Black
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            // Footer Navigation Tabs
            Surface(
                color = Color(0xFF1E293B),
                tonalElevation = 8.dp
            ) {
                val navPadding = WindowInsets.navigationBars.asPaddingValues()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(navPadding)
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FooterTabItem(
                        icon = Icons.Default.Home,
                        label = "হোম",
                        isSelected = currentTab == "home",
                        onClick = { viewModel.setTab("home") },
                        modifier = Modifier.testTag("tab_home")
                    )
                    FooterTabItem(
                        icon = Icons.AutoMirrored.Default.Help,
                        label = "গাইড",
                        isSelected = currentTab == "guide",
                        onClick = { viewModel.setTab("guide") },
                        modifier = Modifier.testTag("tab_guide")
                    )
                    FooterTabItem(
                        icon = Icons.Default.AccountCircle,
                        label = "প্রোফাইল",
                        isSelected = currentTab == "profile",
                        onClick = { viewModel.setTab("profile") },
                        modifier = Modifier.testTag("tab_profile")
                    )
                }
            }
        },
        containerColor = Color(0xFF0F172A)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                "home" -> HomeScreenContent(
                    currentUserFullName = currentUser?.fullName ?: "ইউজার",
                    webApps = webApps,
                    onCreateClick = { showCreateDialog = true },
                    onAppSelect = { viewModel.selectWebApp(it) },
                    onAppDelete = { viewModel.deleteWebApp(it) }
                )
                "guide" -> GuideScreenContent()
                "profile" -> ProfileScreenContent(
                    currentUserFullName = currentUser?.fullName ?: "সম্মানিত ইউজার",
                    currentUserUsername = currentUser?.username ?: "",
                    totalApps = webApps.size,
                    onLogoutClick = { viewModel.logout() }
                )
            }

            // Create App Dialog
            if (showCreateDialog) {
                CreateAppDialog(
                    onDismiss = { showCreateDialog = false },
                    onSave = { name, url, icon, color, pkg ->
                        viewModel.createWebApp(name, url, icon, color, pkg)
                        showCreateDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun FooterTabItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(80.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color(0xFF22D3EE) else Color(0xFF94A3B8),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp
            ),
            color = if (isSelected) Color(0xFF22D3EE) else Color(0xFF94A3B8)
        )
    }
}

@Composable
fun HomeScreenContent(
    currentUserFullName: String,
    webApps: List<WebApp>,
    onCreateClick: () -> Unit,
    onAppSelect: (WebApp) -> Unit,
    onAppDelete: (WebApp) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- HEADER PROMO / NOTIFICATION BANNER ("প্রচারে থাকবে") ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF1E1B4B), // Deep Indigo
                                Color(0xFF0F172A)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = "প্রচার",
                            tint = Color(0xFFE11D48), // Rose Accent
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "সীমিত সময়ের বিশেষ অফার!",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFFDA4AF)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ফ্রি মেম্বারশিপে যতখুশি ওয়েবসাইটকে মোবাইলের মত করে অ্যাপে রুপান্তর করুন! কোন অ্যাড নেই, কোনো হাইড করা চার্জ নেই।",
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                        color = Color(0xFFCBD5E1) // Slate 300
                    )
                }
            }
        }

        // Action Header with create button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "আপনার তৈরি অ্যাপস (${webApps.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )

            Button(
                onClick = onCreateClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF22D3EE),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("onboard_create_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "অ্যাপ তৈরি",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        // --- BODY WEB-APP GRID LIST ---
        if (webApps.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Extension,
                        contentDescription = null,
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "কোন অ্যাপ্লিকেশন ড্যাশবোর্ডে নেই!",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "উপরের '+' বা 'অ্যাপ তৈরি' বাটনে ক্লিক করে ওয়েবসাইট এবং প্যাকেজ নাম দিয়ে প্রথম অ্যাপ তৈরি করে ফেলুন।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1), // Detailed standard card list is superior
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("web_apps_list")
            ) {
                items(webApps, key = { it.id }) { app ->
                    WebAppCard(
                        app = app,
                        onClick = { onAppSelect(app) },
                        onDeleteClick = { onAppDelete(app) }
                    )
                }
            }
        }
    }
}

@Composable
fun WebAppCard(
    app: WebApp,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // Map chosen string colors to actual UI Colors
    val cardAccentColor = remember(app.iconColor) {
        try {
            Color(android.graphics.Color.parseColor(app.iconColor))
        } catch (e: Exception) {
            Color(0xFF22D3EE)
        }
    }

    val iconVector = remember(app.iconName) {
        getVectorIconByName(app.iconName)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stylized Launcher Icon Frame
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(14.dp),
                color = cardAccentColor.copy(alpha = 0.2f),
                border = BoxBorder(1.dp, cardAccentColor.copy(alpha = 0.5f)) // fake helper
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = "App Icon",
                        tint = cardAccentColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Info Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = app.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = app.packageName,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                    color = Color(0xFF22D3EE).copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = app.url,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF94A3B8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Individual Actions
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.testTag("delete_app_${app.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete App",
                    tint = Color(0xFFEF4444)
                )
            }
        }
    }
}

private fun BoxBorder(width: androidx.compose.ui.unit.Dp, color: Color) = androidx.compose.foundation.BorderStroke(width, color)

@Composable
fun GuideScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "মোবাইল অ্যাপ কনভার্টার গাইডলাইন",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GuideSection(
                    title = "১. ওয়েবসাইট লিঙ্ক (URL) কি?",
                    body = "যে ওয়েবসাইটটি আপনি অ্যাপে রুপান্তর করতে চান। যেমন: https://www.google.com। আপনি শুধু google.com লিখলেও আমাদের সিস্টেম নিজে থেকেই সুরক্ষতি https প্রোটোকল যুক্ত করে নিবে।"
                )

                GuideSection(
                    title = "২. প্যাকেজ নাম (Package Name) কি?",
                    body = "একটি অ্যান্ড্রয়েড অ্যাপ্লিকেশনের অনন্য আইডেন্টিফায়ার বা পরিচয়পত্র। এটি সাধারণত 'com.domain.appname' ফরম্যাটে লেখা হয়ে থাকে। উদাহরণস্বরূপ: com.facebook.lite বা com.webtoapp.google।"
                )

                GuideSection(
                    title = "৩. অ্যাপ আইকন নির্ধারণ",
                    body = "পছন্দসই বিভিন্ন ক্যাটাগরির উপর নির্ভর করে যেমন: মেসেঞ্জিং, শপিং বা ট্রাভেলের জন্য চমৎকার সব বিল্ট-ইন গ্লিফ আইকন এবং সেই সাথে নিয়ন রঙসমূহ নির্বাচন করা যাবে যা স্ক্রিনে ভাসবে।"
                )
            }
        }
    }
}

@Composable
fun GuideSection(title: String, body: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF22D3EE),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 18.sp),
            color = Color(0xFFCBD5E1)
        )
    }
}

@Composable
fun ProfileScreenContent(
    currentUserFullName: String,
    currentUserUsername: String,
    totalApps: Int,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Aesthetic user Avatar
        Surface(
            modifier = Modifier.size(90.dp),
            shape = CircleShape,
            color = Color(0xFF22D3EE).copy(alpha = 0.2f),
            border = BoxBorder(2.dp, Color(0xFF22D3EE))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color(0xFF22D3EE),
                    modifier = Modifier.size(54.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = currentUserFullName,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )

        Text(
            text = "@$currentUserUsername",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Info Statistics
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "মোট রূপান্তরের সংখ্যা",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = "$totalApps টি অ্যাপ",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF22D3EE)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Logout
        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEF4444).copy(alpha = 0.15f),
                contentColor = Color(0xFFFCA5A5)
            ),
            shape = RoundedCornerShape(12.dp),
            border = BoxBorder(1.dp, Color(0xFFEF4444).copy(alpha = 0.4f)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("logout_button")
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "নিরাপদে লগআউট করুন",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAppDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, url: String, icon: String, color: String, pkg: String) -> Unit
) {
    var appName by remember { mutableStateOf("") }
    var websiteUrl by remember { mutableStateOf("") }
    var packageName by remember { mutableStateOf("") }

    // Icon & Color list presets
    val presetIcons = listOf(
        "Globe" to "🌐",
        "Shop" to "🛒",
        "Chat" to "💬",
        "Code" to "💻",
        "School" to "🎓",
        "Book" to "📚",
        "Travel" to "✈️",
        "Game" to "🎮"
    )
    val presetColors = listOf(
        "Azure" to "#0284C7",
        "Mint" to "#10B981",
        "Amber" to "#F59E0B",
        "Rose" to "#F43F5E",
        "Violet" to "#8B5CF6",
        "Orange" to "#F97316"
    )

    var selectedIconName by remember { mutableStateOf("Globe") }
    var selectedColorCode by remember { mutableStateOf("#0284C7") }

    // Auto package generation based on App Name
    LaunchedEffect(appName) {
        if (packageName.isBlank() || packageName.startsWith("com.webtoapp.")) {
            val sanitized = appName.lowercase()
                .replace("[^a-zA-Z]".toRegex(), "")
            packageName = if (sanitized.isEmpty()) "" else "com.webtoapp.$sanitized"
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF1E293B),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "নতুন মোবাইল অ্যাপ তৈরি",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )

                // App Name
                OutlinedTextField(
                    value = appName,
                    onValueChange = { appName = it },
                    label = { Text("অ্যাপের নাম (যেমন: গুগল)", color = Color(0xFF94A3B8)) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF22D3EE),
                        unfocusedBorderColor = Color(0xFF475569)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("field_app_name")
                )

                // Web link
                OutlinedTextField(
                    value = websiteUrl,
                    onValueChange = { websiteUrl = it },
                    label = { Text("ওয়েবসাইটের লিঙ্ক (URL Link)", color = Color(0xFF94A3B8)) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    singleLine = true,
                    placeholder = { Text("https://google.com", color = Color(0xFF64748B)) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF22D3EE),
                        unfocusedBorderColor = Color(0xFF475569)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("field_website_url")
                )

                // Package name
                OutlinedTextField(
                    value = packageName,
                    onValueChange = { packageName = it },
                    label = { Text("অ্যাপ প্যাকেজ নাম (Package Name)", color = Color(0xFF94A3B8)) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    singleLine = true,
                    placeholder = { Text("com.webtoapp.google", color = Color(0xFF64748B)) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF22D3EE),
                        unfocusedBorderColor = Color(0xFF475569)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("field_package_name")
                )

                // App Icon Selector
                Text(
                    text = "অ্যাপ আইকন চিহ্ণ নির্বাচন করুন:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presetIcons.forEach { (name, emoji) ->
                        val isSelected = selectedIconName == name
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFF22D3EE) else Color(0xFF334155),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedIconName = name }
                        ) {
                            Text(
                                text = emoji,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }

                // App Icon Color Theme Selector
                Text(
                    text = "অ্যাপ আইকনের কালার থিম:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presetColors.forEach { (name, hex) ->
                        val isSelected = selectedColorCode == hex
                        val parsedColor = Color(android.graphics.Color.parseColor(hex))
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(parsedColor, CircleShape)
                                .clickable { selectedColorCode = hex }
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(Color.White, CircleShape)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Actions Save/Cancel
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("বাতিল করুন", color = Color(0xFF94A3B8))
                    }

                    Button(
                        onClick = {
                            if (appName.isNotBlank() && websiteUrl.isNotBlank()) {
                                onSave(appName, websiteUrl, selectedIconName, selectedColorCode, packageName)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF22D3EE),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("submit_create_app"),
                        enabled = appName.isNotBlank() && websiteUrl.isNotBlank()
                    ) {
                        Text("তৈরি করুন", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

private fun getVectorIconByName(name: String): ImageVector {
    return when (name) {
        "Globe" -> Icons.Default.Language
        "Shop" -> Icons.Default.ShoppingCart
        "Chat" -> Icons.Default.ChatBubble
        "Code" -> Icons.Default.Code
        "School" -> Icons.Default.School
        "Book" -> Icons.Default.Book
        "Travel" -> Icons.Default.FlightTakeoff
        "Game" -> Icons.Default.Gamepad
        else -> Icons.Default.Language
    }
}
