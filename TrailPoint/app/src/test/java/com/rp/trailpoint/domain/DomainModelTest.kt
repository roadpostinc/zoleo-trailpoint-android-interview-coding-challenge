package com.rp.trailpoint.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class DomainModelTest {

    @Test
    fun `message type has three cases`() {
        assertEquals(
            setOf(MessageType.SOS, MessageType.CHECK_IN, MessageType.TEXT),
            MessageType.entries.toSet(),
        )
    }

    @Test
    fun `device defaults to no active SOS alert`() {
        val device = Device(
            id = "device-1",
            name = "Quiet Unit",
            activeSosAlert = false,
            lastSeen = 1L,
        )

        assertFalse(device.activeSosAlert)
    }
}
