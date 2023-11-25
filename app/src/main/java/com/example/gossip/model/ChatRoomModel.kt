package com.example.gossip.model

import com.google.firebase.Timestamp

data class ChatRoomModel(
    val chatRoom: ChatRoom,
    val key: String? = ""
){
    data class ChatRoom(
        val members: List<String> = emptyList(),
        val chatRoomId: String = "",
        val lastMessageSenderId: String = "",
        val lastMessage: String = "",
        val lastUpdated: Timestamp = Timestamp.now()
    )
}