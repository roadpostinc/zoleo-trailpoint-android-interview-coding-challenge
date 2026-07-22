package com.rp.trailpoint.ui.devicedetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rp.trailpoint.ui.common.formatTimestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailScreen(
    viewModel: DeviceDetailViewModel,
    modifier: Modifier = Modifier,
) {
    val device by viewModel.device.collectAsState()
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(device?.name ?: "Device") }) },
    ) { innerPadding ->
        val current = device
        if (current == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text("Loading device...")
            }
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            Text(text = current.name, style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Last seen ${formatTimestamp(current.lastSeen)}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
