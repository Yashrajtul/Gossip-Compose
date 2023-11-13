package com.example.gossip.firestoredb.repository

import com.example.gossip.model.ChatRoomModel
import com.example.gossip.model.ChatMessageModel
import com.example.gossip.utils.ResultState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: StorageReference
) : ChatRepository{
    override fun createChatRoom(chat: ChatRoomModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.collection("chatroom")
            .document(chat.key!!)
            .set(chat.chat!!)
            .addOnCompleteListener {
                trySend(ResultState.Success("Chatroom Created!"))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun getChatRoom(chatRoomId: String): Flow<ResultState<ChatRoomModel.Chat?>> = callbackFlow{
        trySend(ResultState.Loading)

        db.collection("chatroom")
            .document(chatRoomId)
            .get()
            .addOnCompleteListener {
                val chatRoom = it.result.toObject(ChatRoomModel.Chat::class.java)
                trySend(ResultState.Success(chatRoom))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun sendMessage(message: ChatMessageModel.Message): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.collection("messages")
            .add(message)
            .addOnCompleteListener {
                trySend(ResultState.Success(it.result.id))
            }
            .addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun deleteMessage(key: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.collection("messages")
            .document(key)
            .delete()
            .addOnCompleteListener {

                trySend(ResultState.Success("Message deleted successfully..."))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }
}