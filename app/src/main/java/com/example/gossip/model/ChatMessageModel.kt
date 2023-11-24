package com.example.gossip.model

import com.google.firebase.Timestamp

data class ChatMessageModel(
    val message: Message = Message(),
    val key: String = ""
){
    data class Message(
        val senderId: String = "",
        val type: String = MessageType.TEXT.name,
        val messageContent: String = "",
        val status: String = "",
        val timestamp: Timestamp = Timestamp.now()
    )
}
