package com.example.gossip.firestoredb.repository

import com.example.gossip.model.ChatRoomModel
import com.example.gossip.model.ChatMessageModel
import com.example.gossip.model.UserDataModelResponse
import com.example.gossip.util.Resource
import com.example.gossip.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getUserData(
        key: String
    ): UserDataModelResponse.User?

    fun updateChatRoom(
        chatRoom: ChatRoomModel.ChatRoom
    ): Resource<String>

    suspend fun getChatRoom(
        chatRoomId: String
    ): ChatRoomModel.ChatRoom?

    suspend fun sendMessage(
        chatRoomId: String,
        message: ChatMessageModel.Message
    ): Resource<Unit>

    fun getMessages(
        chatRoomId: String
    ): Flow<ResultState<List<ChatMessageModel.Message?>>>

    fun deleteMessage(
        key: String
    ): Resource<String>
}