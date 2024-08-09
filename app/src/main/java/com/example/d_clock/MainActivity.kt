package com.example.d_clock

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.d_clock.ui.theme.DClockTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DClockTheme {
                val postNotificationPermissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
                val timeNotificationService = TimeNotificationService(this)

                LaunchedEffect(key1 = true) {
                    if (!postNotificationPermissionState.status.isGranted) {
                        postNotificationPermissionState.launchPermissionRequest()
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button (
                            onClick = { timeNotificationService.showNotification() }
                        ) {
                            Text("show notification")
                        }

                        Button (
                            onClick = { timeNotificationService.deleteNotification() }
                        ) {
                            Text("cancel notification")
                        }
                    }
                }
            }
        }
    }
}
