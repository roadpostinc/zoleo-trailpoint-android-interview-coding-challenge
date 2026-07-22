package com.rp.trailpoint.data

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private const val MESSAGES_JSON = """
    [
      {"id":"message-1","deviceId":"device-1","type":"checkIn","body":"All clear.","timestamp":"1000"},
      {"id":"message-2","deviceId":"device-2","type":"sos","body":"Need assistance.","timestamp":"2000"},
      {"id":"message-3","deviceId":"device-1","type":"text","body":"On my way.","timestamp":"3000"}
    ]
"""

class MessageRepositoryTest {

    @Test
    fun `fetchMessages decodes all messages`() = runTest {
        val repository = MessageRepository(
            httpClient = stubHttpClient("/messages" to MESSAGES_JSON),
        )

        val messages = repository.fetchMessages()

        assertEquals(3, messages.size)
        assertEquals(1000L, messages.first().timestamp)
    }

    @Test
    fun `fetchMessages returns empty for a non-200 response`() = runTest {
        val repository = MessageRepository(
            httpClient = stubHttpClient("/messages" to MESSAGES_JSON, responseCode = 201),
        )

        assertTrue(repository.fetchMessages().isEmpty())
    }

    @Test
    fun `fetchMessages accepts an empty list`() = runTest {
        val repository = MessageRepository(
            httpClient = stubHttpClient("/messages" to "[]"),
        )

        assertTrue(repository.fetchMessages().isEmpty())
    }
}
