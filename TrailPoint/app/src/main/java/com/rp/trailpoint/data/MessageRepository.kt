package com.rp.trailpoint.data

import com.rp.trailpoint.domain.Message
import com.rp.trailpoint.domain.MessageType
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

fun interface MessageSource {
    suspend fun fetchMessages(): List<Message>
}

@Serializable
private data class MessageDto(
    val id: String,
    val deviceId: String,
    val type: String,
    val body: String,
    val timestamp: String,
)

private fun MessageDto.toDomain() = Message(
    id = id,
    deviceId = deviceId,
    type = when (type) {
        "sos" -> MessageType.SOS
        "checkIn" -> MessageType.CHECK_IN
        "text" -> MessageType.TEXT
        else -> null
    }!!,
    body = body,
    timestamp = timestamp.toLongOrNull() ?: 0L,
)

class MessageRepository(
    private val httpClient: OkHttpClient = OkHttpClient(),
) : MessageSource {

    override suspend fun fetchMessages(): List<Message> = withContext(Dispatchers.IO) {
        val body = try {
            val path = "/messages"
            val request = Request.Builder().url("${NetworkConfig.BASE_URL}$path").build()
            httpClient.newCall(request).execute().use { response ->
                if (response.code != 200) {
                    throw RepositoryException("GET $path failed with HTTP ${response.code}")
                }
                response.body?.string()
                    ?: throw RepositoryException("GET $path returned an empty body")
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: IOException) {
            return@withContext emptyList()
        } catch (_: RepositoryException) {
            return@withContext emptyList()
        }
        Json.decodeFromString<List<MessageDto>>(body).map { it.toDomain() }
    }
}
