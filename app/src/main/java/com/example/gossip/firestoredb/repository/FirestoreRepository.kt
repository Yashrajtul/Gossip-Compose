package com.example.gossip.firestoredb.repository

import com.example.gossip.firestoredb.UserDataModelResponse
import com.example.gossip.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    fun insertUser(
        user: UserDataModelResponse
    ): Flow<ResultState<String>>

    fun getUser(): Flow<ResultState<List<UserDataModelResponse>>>

    fun delete(
        key: String
    ): Flow<ResultState<String>>

    fun updateUser(
        res: UserDataModelResponse
    ): Flow<ResultState<String>>
}