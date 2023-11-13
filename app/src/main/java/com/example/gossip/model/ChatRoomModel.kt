package com.example.gossip.model

import com.google.firebase.Timestamp

data class ChatRoomModel(
    val chat: Chat?,
    val key: String? = ""
){
    data class Chat(
        val members: List<String>? = emptyList(),
        val messages: List<String>? = emptyList(),
        val chatRoomId: String? = "",
        val lastMessageSenderId: String = "",
        val lastUpdated: Timestamp? = null
    )
}