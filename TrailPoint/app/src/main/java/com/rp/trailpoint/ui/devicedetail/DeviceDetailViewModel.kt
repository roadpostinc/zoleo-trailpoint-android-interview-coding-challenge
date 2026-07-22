package com.rp.trailpoint.ui.devicedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rp.trailpoint.data.DeviceRepository
import com.rp.trailpoint.domain.Device
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeviceDetailViewModel(
    private val repository: DeviceRepository,
    private val deviceId: String,
) : ViewModel() {

    private val _device = MutableStateFlow<Device?>(null)
    val device: StateFlow<Device?> = _device.asStateFlow()

    init {
        viewModelScope.launch {
            _device.value = repository.getStatus(deviceId)
        }
    }

    companion object {
        fun factory(repository: DeviceRepository, deviceId: String) = viewModelFactory {
            initializer { DeviceDetailViewModel(repository, deviceId) }
        }
    }
}
