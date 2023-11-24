package com.example.gossip.data.remote

import com.example.gossip.data.remote.dto.MessageDto
import com.example.gossip.domain.model.Message
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class MessageServiceImpl(
    private val client: HttpClient
) : MessageService{
    override suspend fun getAllMessage(roomId: String): List<Message> {
        return try {
//            client.get(MessageService.Endpoints.GetAllMessages.url).body<List<MessageDto>>()
//                .map { it.toMessage() }
            client.get("${MessageService.Endpoints.GetAllMessages.url}/$roomId").body<List<MessageDto>>()
                .map { it.toMessage() }
        }catch (e: Exception){
            emptyList()
        }
    }
}