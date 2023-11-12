package com.example.gossip.firestoredb.repository

import com.example.gossip.model.ChatRoom
import com.example.gossip.model.Messages
import com.example.gossip.utils.ResultState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: StorageReference
) : ChatRepository{
    override fun createChatRoom(chat: ChatRoom.Chat): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.collection("chatroom")
            .add(chat)
            .addOnCompleteListener {
                trySend(ResultState.Success(it.result.id))

            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun sendMessage(message: Messages.Message): Flow<ResultState<String>> = callbackFlow {
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