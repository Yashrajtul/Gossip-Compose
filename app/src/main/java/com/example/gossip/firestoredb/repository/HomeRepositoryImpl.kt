package com.example.gossip.firestoredb.repository

import android.net.Uri
import android.util.Log
import com.example.gossip.model.ChatRoomModel
import com.example.gossip.model.UserDataModelResponse
import com.example.gossip.util.Resource
import com.example.gossip.utils.ResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: StorageReference
) : HomeRepository {
    override suspend fun getChatUsers(userId: String): Flow<ResultState<List<ChatRoomModel.ChatRoom>>> = callbackFlow{
        trySend(ResultState.Loading)

        db.collection("chatrooms")
            .whereArrayContains("members",userId)
            .orderBy("lastUpdated", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(ResultState.Failure(error))
                    return@addSnapshotListener
                }
                val chatRooms = value?.toObjects(ChatRoomModel.ChatRoom::class.java)!!
                Log.d("chatRooms", chatRooms.toString())
                trySend(ResultState.Success(chatRooms))
            }
//            .get()
//            .addOnSuccessListener {
//                val chatRooms = it.toObjects(ChatRoomModel.ChatRoom::class.java)
//                Log.d("chatRooms", chatRooms.toString())
//                trySend(ResultState.Success(chatRooms))
//            }.addOnFailureListener {
//                trySend(ResultState.Failure(it))
//            }


//        try {
//            val chatRooms = db.collection("chatrooms")
//                .whereArrayContains("members",userId)
//                .orderBy("lastUpdated", Query.Direction.DESCENDING)
//                .get()
//                .await()
//                .toObjects(ChatRoomModel.ChatRoom::class.java)
//            Log.d("chatRooms", chatRooms.toString())
//            val users = chatRooms.map { chatRoom ->
//                val user = getOtherUser(chatRoom.members, userId)
//                user.userRoom = chatRoom
//                user
//
//            }
//            Log.d("user", users.toString())
//            trySend(ResultState.Success(users))
//
//        }catch (e: Exception){
//            trySend(ResultState.Failure(e))
//        }

        awaitClose {
            close()
        }
    }

    override suspend fun getOtherUser(userIds: List<String>, myUserId: String): UserDataModelResponse {
        val userId = if (userIds.get(0).equals(myUserId) ) userIds.get(1) else userIds.get(0)
        val user = db.collection("user")
            .document(userId)
            .get()
            .await()
            .toObject(UserDataModelResponse.User::class.java)!!

        return UserDataModelResponse(
            user = user,
            profilePic = getProfilePic(userId),
            key = userId
        )
    }

    override suspend fun getProfilePic(userId: String): Uri? {
        return try {
            storage.child("profilePictures")
                .child(userId)
                .downloadUrl
                .await()!!
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }


}