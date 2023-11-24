package com.example.gossip.data.remote

import com.example.gossip.domain.model.Message

interface MessageService {

    suspend fun getAllMessage(roomId: String): List<Message>

    companion object {
        const val BASE_URL = "http://192.168.200.224:8080"
    }

    sealed class Endpoints(val url: String) {
        object GetAllMessages: Endpoints("$BASE_URL/messages")
    }
}