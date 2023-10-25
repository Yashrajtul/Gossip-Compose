package com.example.gossip.firestoredb

data class Messages(
    val message: Message?,
    val key: String? = ""
){
    data class Message(
        val sender: String? = "",
        val type: String? = "",
        val text: String? = "",
        val timestamp: Int
    )
}
