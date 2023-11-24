package com.example.gossip.firestoredb.repository

import com.example.gossip.model.ChatRoomModel
import com.example.gossip.model.ChatMessageModel
import com.example.gossip.model.UserDataModelResponse
import com.example.gossip.util.Resource
import com.example.gossip.utils.ResultState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: StorageReference
) : ChatRepository{

    override suspend fun getUserData(key: String): UserDataModelResponse.User? {
        return try {
            db.collection("user")
                    .document(key)
                    .get()
                    .await()
                    .toObject(UserDataModelResponse.User::class.java)
            }catch (e: Exception){
                e.printStackTrace()
                null
            }
        }
    override fun updateChatRoom(chatRoom: ChatRoomModel.ChatRoom): Resource<String>{
        return try {
            db.collection("chatrooms")
                .document(chatRoom.chatRoomId)
                .set(chatRoom)
            Resource.Success("")
        }catch (e: Exception){
            Resource.Error(e.localizedMessage?: "Unknown error")
        }
    }

    override suspend fun getChatRoom(chatRoomId: String): ChatRoomModel.ChatRoom? {
        return try {
            db.collection("chatrooms")
                .document(chatRoomId)
                .get()
                .await()
                .toObject(ChatRoomModel.ChatRoom::class.java)
        }catch (e: Exception){
            e.printStackTrace()
            null
        }

    }

    override suspend fun sendMessage(chatRoomId: String, message: ChatMessageModel.Message): Resource<Unit> {
        return try {
            db.collection("chatrooms")
                .document(chatRoomId)
                .collection("chats")
                .add(message)
            Resource.Success(Unit)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.localizedMessage?:"Unknown error")
        }
    }

    override fun getMessages(chatRoomId: String): Flow<ResultState<List<ChatMessageModel.Message?>>> = callbackFlow{
        trySend(ResultState.Loading)

        try {
            val messages = db.collection("chatrooms")
                .document(chatRoomId)
                .collection("chats")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects(ChatMessageModel.Message::class.java)
            trySend(ResultState.Success(messages))
        }catch (e: Exception){
            trySend(ResultState.Failure(e))
        }

        awaitClose {
            close()
        }
    }

    override fun deleteMessage(key: String): Resource<String> {
        return try {
            db.collection("messages")
                .document(key)
                .delete()
            Resource.Success("Message deleted successfully...")
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.localizedMessage?: "Unknown error")
        }
    }
}