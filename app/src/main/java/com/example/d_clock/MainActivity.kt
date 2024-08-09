package com.example.d_clock

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

                val postNotificationPermissionState =
                    rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

                LaunchedEffect(key1 = true) {
                    if (!postNotificationPermissionState.status.isGranted) {
                        postNotificationPermissionState.launchPermissionRequest()
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    val timeNotificationService = TimeNotificationService(context)

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button (
                            onClick = {
                                timeNotificationService.showNotification()
                            }
                        ) {
                            Text("show notification")
                        }

                        Button (
                            onClick = {
                                Intent(applicationContext, RunningService::class.java).also {
                                    it.action = RunningService.Actions.START.toString()
                                    startService(it)
                                }
                            }
                        ) {
                            Text ("show foreground notification")
                        }

                        Button (
                            onClick = {
                                Intent(applicationContext, RunningService::class.java).also {
                                    it.action = RunningService.Actions.STOP.toString()
                                    startService(it)
                                }
                            }
                        ) {
                            Text("cancel foreground notification")
                        }

                        Button (
                            onClick = {
                                timeNotificationService.showNotification()
                            }
                        ) {
                            Text("cancel notification")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Options (
    modifier : Modifier,
    start : Intent,
    stop : Intent
) {

}