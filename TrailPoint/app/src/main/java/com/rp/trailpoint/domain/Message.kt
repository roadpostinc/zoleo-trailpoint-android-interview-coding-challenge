package com.rp.trailpoint.domain

data class Message(
    val id: String,
    val deviceId: String,
    val type: MessageType,
    val body: String,
    val timestamp: Long,
    val residual: Any? = null,
)
