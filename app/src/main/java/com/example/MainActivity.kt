package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.WebAppViewer
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Supports full edge-to-edge screens with immersive navigation / status bars
        enableEdgeToEdge()
        
        setContent {
            MyApplicationTheme {
                val currentUser by viewModel.currentUser.collectAsState()
                val selectedWebApp by viewModel.selectedWebApp.collectAsState()

                when {
                    // Match Case 1: An active web app is loaded -> render in full-bleed web sandboxed environment
                    selectedWebApp != null -> {
                        WebAppViewer(
                            app = selectedWebApp!!,
                            onClose = { viewModel.selectWebApp(null) },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    // Match Case 2: No active user session -> render deep Slate Authentication flows
                    currentUser == null -> {
                        LoginScreen(
                            viewModel = viewModel,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    // Match Case 3: Logged in user session active -> render core Dashboard list and tab hubs
                    else -> {
                        DashboardScreen(
                            viewModel = viewModel,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
