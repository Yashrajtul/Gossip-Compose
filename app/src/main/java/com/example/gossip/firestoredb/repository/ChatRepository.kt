package com.example.gossip.firestoredb.repository

import com.example.gossip.model.ChatRoomModel
import com.example.gossip.model.ChatMessageModel
import com.example.gossip.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun createChatRoom(
        chat: ChatRoomModel
    ): Flow<ResultState<String>>

    fun getChatRoom(
        chatRoomId: String
    ): Flow<ResultState<ChatRoomModel.Chat?>>

    fun sendMessage(
        message: ChatMessageModel.Message
    ): Flow<ResultState<String>>

    fun deleteMessage(
        key: String
    ): Flow<ResultState<String>>
}