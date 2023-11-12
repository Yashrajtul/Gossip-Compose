package com.example.gossip.firestoredb.repository

import com.example.gossip.model.ChatRoom
import com.example.gossip.model.Messages
import com.example.gossip.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun createChatRoom(
        chat: ChatRoom.Chat
    ): Flow<ResultState<String>>

    fun sendMessage(
        message: Messages.Message
    ): Flow<ResultState<String>>

    fun deleteMessage(
        key: String
    ): Flow<ResultState<String>>
}