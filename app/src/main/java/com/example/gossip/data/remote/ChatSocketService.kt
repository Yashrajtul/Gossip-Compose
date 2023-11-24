package com.example.gossip.data.remote

import com.example.gossip.domain.model.Message
import com.example.gossip.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {

    suspend fun initSession(
        userId: String,
        roomId: String
    ): Resource<Unit>

    suspend fun sendMessage(message: String)

    fun observeMessage(): Flow<Message>

    suspend fun closeSession()

    companion object {
        const val BASE_URL = "ws://192.168.200.224:8080"
    }

    sealed class Endpoints(val url: String) {
//        object ChatSocket: Endpoints("$BASE_URL/chat-socket")
        object ChatSocket: Endpoints("$BASE_URL/ws")
    }
}