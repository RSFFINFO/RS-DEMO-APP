package com.example.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.WebApp

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebAppViewer(
    app: WebApp,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var webView: WebView? by remember { mutableStateOf(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var currentUrl by remember { mutableStateOf(app.url) }

    // Intercept hardware Android back click to navigate backward in the website history first
    BackHandler(enabled = canGoBack) {
        webView?.goBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Surface(
                color = Color(0xFF1E293B),
                tonalElevation = 6.dp
            ) {
                val statusPadding = WindowInsets.statusBars.asPaddingValues()
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(statusPadding)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Exit Back Icon
                        IconButton(
                            onClick = {
                                if (webView?.canGoBack() == true) {
                                    webView?.goBack()
                                } else {
                                    onClose()
                                }
                            },
                            modifier = Modifier.testTag("wv_back_header")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // App Packaging detail text
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
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp
                                ),
                                color = Color(0xFF22D3EE),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Web Control buttons: Refresh, Close
                        IconButton(onClick = { webView?.reload() }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Webpage",
                                tint = Color(0xFFCBD5E1)
                            )
                        }

                        IconButton(
                            onClick = onClose,
                            modifier = Modifier.testTag("wv_close_webapp")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close WebApp",
                                tint = Color(0xFFEF4444)
                            )
                        }
                    }

                    // Bottom progress indicator during webpage load
                    if (isLoading) {
                        LinearProgressIndicator(
                            color = Color(0xFF22D3EE),
                            trackColor = Color(0xFF334155),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFF0F172A)
    ) { innerPadding ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("webview_sandbox"),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    // Enable Javascript and secure features required for rich modern websites
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        builtInZoomControls = true
                        displayZoomControls = false
                        mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                            if (url != null) currentUrl = url
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                            canGoBack = view?.canGoBack() ?: false
                        }
                    }

                    webChromeClient = object : WebChromeClient() {
                        // Optional handling
                    }

                    loadUrl(app.url)
                    webView = this
                }
            },
            update = { view ->
                // Ensure correct instance reference
                webView = view
            }
        )
    }
}
