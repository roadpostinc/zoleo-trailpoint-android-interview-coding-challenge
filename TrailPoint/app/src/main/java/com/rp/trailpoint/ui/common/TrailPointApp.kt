package com.rp.trailpoint.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rp.trailpoint.data.DeviceRepository
import com.rp.trailpoint.data.MessageRepository
import com.rp.trailpoint.ui.devicedetail.DeviceDetailScreen
import com.rp.trailpoint.ui.devicedetail.DeviceDetailViewModel
import com.rp.trailpoint.ui.devices.DeviceListScreen
import com.rp.trailpoint.ui.devices.DeviceListViewModel
import com.rp.trailpoint.ui.messages.MessageListScreen
import com.rp.trailpoint.ui.messages.MessagesViewModel

private const val ROUTE_DEVICES = "devices"
private const val ROUTE_MESSAGES = "messages"
private const val ROUTE_DEVICE_DETAIL = "deviceDetail/{deviceId}"

@Composable
fun TrailPointApp(
    deviceRepository: DeviceRepository,
    messageRepository: MessageRepository,
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { TrailPointBottomBar(navController) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROUTE_DEVICES,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(ROUTE_DEVICES) {
                val viewModel: DeviceListViewModel = viewModel(
                    factory = DeviceListViewModel.factory(deviceRepository),
                )
                DeviceListScreen(
                    viewModel = viewModel,
                    onDeviceClick = { id -> navController.navigate("deviceDetail/$id") },
                )
            }
            composable(ROUTE_DEVICE_DETAIL) { backStackEntry ->
                val deviceId = backStackEntry.arguments?.getString("deviceId")
                if (deviceId != null) {
                    val viewModel: DeviceDetailViewModel = viewModel(
                        factory = DeviceDetailViewModel.factory(deviceRepository, deviceId),
                    )
                    DeviceDetailScreen(viewModel = viewModel)
                }
            }
            composable(ROUTE_MESSAGES) {
                val viewModel: MessagesViewModel = viewModel(
                    factory = MessagesViewModel.factory(messageRepository),
                )
                MessageListScreen(viewModel = viewModel)
            }
        }
    }
}

private data class BottomDestination(val route: String, val label: String)

private val bottomDestinations = listOf(
    BottomDestination(ROUTE_DEVICES, "Devices"),
    BottomDestination(ROUTE_MESSAGES, "Messages"),
)

@Composable
private fun TrailPointBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    NavigationBar {
        bottomDestinations.forEach { destination ->
            val selected = currentDestination?.hierarchy?.any {
                it.route == destination.route
            } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {},
                label = { Text(destination.label) },
            )
        }
    }
}
