package com.example.gossip.firebaseRealtimeDb

data class RealtimeModelResponse(
    val userChats: UserChats?,
    val key: String?

){
    data class UserChats(
        val chatRooms: List<String>? = emptyList(),
        val chatter: String? = ""
    )
}
