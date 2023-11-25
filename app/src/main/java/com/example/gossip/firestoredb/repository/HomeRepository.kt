package com.example.gossip.firestoredb.repository

import android.net.Uri
import com.example.gossip.model.ChatRoomModel
import com.example.gossip.model.UserDataModelResponse
import com.example.gossip.util.Resource
import com.example.gossip.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getChatUsers(userId: String): Flow<ResultState<List<ChatRoomModel.ChatRoom>>>
    suspend fun getOtherUser(userIds: List<String>, myUserId: String): UserDataModelResponse
    suspend fun getProfilePic(userId: String): Uri?
}