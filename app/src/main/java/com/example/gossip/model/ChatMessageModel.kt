package com.example.gossip.model

import com.google.firebase.Timestamp

data class ChatMessageModel(
    val message: Message?,
    val key: String? = ""
){
    data class Message(
        val senderId: String? = "",
        val type: String? = "",
        val text: String? = "",
        val timestamp: Timestamp
    )
}
