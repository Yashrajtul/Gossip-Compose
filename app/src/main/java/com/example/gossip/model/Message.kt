package com.example.gossip.model

import java.util.Date

data class Message(
    val messageId: String,
    val message: String,
    val time: Date
)
