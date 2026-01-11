package com.example.healthapp

data class Message(
    var messageId: String? = null,
    val senderId: String? = null,
    val senderName: String? = null,
    val messageText: String? = null,
    val timestamp: Long? = null
)
