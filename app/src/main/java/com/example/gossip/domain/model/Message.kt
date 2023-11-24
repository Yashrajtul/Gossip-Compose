package com.example.gossip.domain.model

data class Message(
    val messageContent: String,
    val time: Long,
    val userId: String
)
