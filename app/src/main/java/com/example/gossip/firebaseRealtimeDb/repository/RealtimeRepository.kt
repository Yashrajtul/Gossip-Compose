package com.example.gossip.firebaseRealtimeDb.repository

import com.example.gossip.firebaseRealtimeDb.RealtimeModelResponse
import com.example.gossip.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface RealtimeRepository {
    fun insertUserChats(
        userChats: RealtimeModelResponse
    ): Flow<ResultState<String>>

    fun getUser(): Flow<ResultState<List<RealtimeModelResponse>>>

    fun delete(
        key: String
    ): Flow<ResultState<String>>

    fun updateUser(
        res: RealtimeModelResponse
    ): Flow<ResultState<String>>


}