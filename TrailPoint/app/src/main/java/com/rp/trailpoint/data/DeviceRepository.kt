package com.rp.trailpoint.data

import com.rp.trailpoint.domain.Device
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

@Serializable
private data class DeviceDto(
    val id: String,
    val name: String,
    val hasActiveSosAlert: Boolean,
    val lastSeen: Long,
)

private fun DeviceDto.toDomain() = Device(
    id = id,
    name = name,
    activeSosAlert = hasActiveSosAlert,
    lastSeen = lastSeen,
)

class DeviceRepository(private val httpClient: OkHttpClient = OkHttpClient()) {

    private val json = Json { ignoreUnknownKeys = true }
    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    suspend fun loadDevices(): List<Device> {
        val loadedDevices = try {
            json.decodeFromString<List<DeviceDto>>(get("/devices"))
                .map { it.toDomain() }
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: Exception) {
            emptyList()
        }
        _devices.value = loadedDevices
        return loadedDevices
    }

    suspend fun getStatus(deviceId: String): Device? {
        return try {
            json.decodeFromString<DeviceDto>(get("/devices/$deviceId")).toDomain()
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: Exception) {
            null
        }
    }

    private suspend fun get(path: String): String = withContext(Dispatchers.IO) {
        val request = Request.Builder().url("${NetworkConfig.BASE_URL}$path").build()
        httpClient.newCall(request).execute().use { response ->
            if (response.code != 200) {
                throw RepositoryException("GET $path failed with HTTP ${response.code}")
            }
            response.body?.string()
                ?: throw RepositoryException("GET $path returned an empty body")
        }
    }
}
