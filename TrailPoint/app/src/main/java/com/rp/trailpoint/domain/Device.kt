package com.rp.trailpoint.domain

data class Device(
    val id: String,
    val name: String,
    val activeSosAlert: Boolean,
    val lastSeen: Long,
)
