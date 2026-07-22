package com.rp.trailpoint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rp.trailpoint.data.DeviceRepository
import com.rp.trailpoint.data.MessageRepository
import com.rp.trailpoint.ui.common.TrailPointApp
import com.rp.trailpoint.ui.theme.TrailPointTheme

class MainActivity : ComponentActivity() {

    private val deviceRepository = DeviceRepository()
    private val messageRepository = MessageRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrailPointTheme {
                TrailPointApp(
                    deviceRepository = deviceRepository,
                    messageRepository = messageRepository,
                )
            }
        }
    }
}
