package com.rp.trailpoint.ui.devices

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import com.rp.trailpoint.domain.Device
import com.rp.trailpoint.ui.common.formatTimestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceListScreen(
    viewModel: DeviceListViewModel,
    onDeviceClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val devices by viewModel.devices.collectAsState()
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Devices") }) },
    ) { innerPadding ->
        DeviceListContent(
            devices = devices,
            onDeviceClick = onDeviceClick,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun DeviceListContent(
    devices: List<Device>,
    onDeviceClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items = devices, key = { it.id }) { device ->
            DeviceRow(device = device, onClick = { onDeviceClick(device.id) })
            HorizontalDivider()
        }
    }
}

@Composable
private fun DeviceRow(device: Device, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
    ) {
        Text(text = device.name, style = MaterialTheme.typography.titleMedium)
        Text(
            text = "Last seen ${formatTimestamp(device.lastSeen)}",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
