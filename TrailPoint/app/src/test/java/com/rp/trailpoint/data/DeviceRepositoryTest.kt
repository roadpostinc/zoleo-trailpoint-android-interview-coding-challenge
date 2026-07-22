package com.rp.trailpoint.data

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private const val DEVICES_JSON = """
    [
      {"id":"device-1","name":"Basecamp Beacon","hasActiveSosAlert":false,"lastSeen":1000},
      {"id":"device-2","name":"Ridge Runner","hasActiveSosAlert":true,"lastSeen":2000}
    ]
"""

class DeviceRepositoryTest {

    @Test
    fun `loadDevices decodes the device list`() = runTest {
        val repository = DeviceRepository(
            httpClient = stubHttpClient("/devices" to DEVICES_JSON),
        )

        val devices = repository.loadDevices()

        assertEquals(2, devices.size)
        assertFalse(devices.first().activeSosAlert)
    }

    @Test
    fun `getStatus returns a device`() = runTest {
        val body = """
            {"id":"device-2","name":"Ridge Runner","hasActiveSosAlert":true,"lastSeen":9999}
        """.trimIndent()
        val repository = DeviceRepository(
            httpClient = stubHttpClient("/devices/device-2" to body),
        )

        val device = repository.getStatus("device-2")

        assertEquals(9999L, device?.lastSeen)
    }

    @Test
    fun `loadDevices clears devices for a non-200 response`() = runTest {
        val repository = DeviceRepository(
            httpClient = stubHttpClient("/devices" to DEVICES_JSON, responseCode = 201),
        )

        assertTrue(repository.loadDevices().isEmpty())
        assertTrue(repository.devices.value.isEmpty())
    }
}
