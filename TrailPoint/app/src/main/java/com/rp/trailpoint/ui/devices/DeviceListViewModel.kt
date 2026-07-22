package com.rp.trailpoint.ui.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rp.trailpoint.data.DeviceRepository
import com.rp.trailpoint.domain.Device
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeviceListViewModel(private val repository: DeviceRepository) : ViewModel() {

    val devices: StateFlow<List<Device>> = repository.devices

    init {
        viewModelScope.launch { repository.loadDevices() }
    }

    companion object {
        fun factory(repository: DeviceRepository) = viewModelFactory {
            initializer { DeviceListViewModel(repository) }
        }
    }
}
