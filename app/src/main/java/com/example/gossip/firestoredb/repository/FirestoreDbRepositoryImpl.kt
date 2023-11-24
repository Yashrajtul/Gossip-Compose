package com.example.gossip.firestoredb.repository

import android.net.Uri
import com.example.gossip.model.UserDataModelResponse
import com.example.gossip.utils.ResultState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreDbRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: StorageReference
) : FirestoreRepository {
    override fun getUsers(): Flow<ResultState<List<UserDataModelResponse.User>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            db.collection("user")
                .get()
                .addOnSuccessListener {
                    val users: List<UserDataModelResponse.User> =
                        it.toObjects(UserDataModelResponse.User::class.java)
                    trySend(ResultState.Success(users))
                }.addOnFailureListener {
                    trySend(ResultState.Failure(it))
                }

            awaitClose {
                close()
            }
        }

    override fun searchUsers(searchString: String): Flow<ResultState<List<UserDataModelResponse.User>>> =
        callbackFlow {
            trySend(ResultState.Loading)

//            try {

                db.collection("user")
                    .whereGreaterThanOrEqualTo("username", searchString)
                    .get()
                    .addOnSuccessListener {
                        val users: List<UserDataModelResponse.User> =
                            it.toObjects(UserDataModelResponse.User::class.java)
//                val items = it.map { data ->
//                    UserDataModelResponse(
//                        user = UserDataModelResponse.User(
//                            username = data["username"] as String?,
//                            phone = data["phone"] as String?,
//                            userId = data["userId"] as String?,
//                            createdTimestamp = data["createdTimestamp"] as Timestamp
//                        ),
//                        key = data.id
//                    )
//                }
                        trySend(ResultState.Success(users))
                    }.addOnFailureListener {
                        trySend(ResultState.Failure(it))
                    }
//                    .await()

//            }catch (e:Exception){
//                e.printStackTrace()
//                trySend(ResultState.Failure(e))
//            }

            awaitClose {
                close()
            }
        }

    override fun getUserData(key: String): Flow<ResultState<UserDataModelResponse.User?>> =
        callbackFlow {
            trySend(ResultState.Loading)

            try {
                 val user = db.collection("user")
                    .document(key)
                    .get()
                    .await()
                    .toObject(UserDataModelResponse.User::class.java)
                trySend(ResultState.Success(user))
//                    .addOnSuccessListener { data ->
//                        val user: UserDataModelResponse.User? =
//                            data.toObject(UserDataModelResponse.User::class.java)
//                        trySend(ResultState.Success(user))
//                    }.addOnFailureListener {
//                        trySend(ResultState.Failure(it))
//                    }
            }catch (e: Exception){
                trySend(ResultState.Failure(e))
            }

            awaitClose {
                close()
            }
        }

    override fun delete(key: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.collection("user")
            .document(key)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful)
                    trySend(ResultState.Success("Deleted Successfully.."))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun uploadPic(image: Uri, key: String): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            storage.child("profilePictures/${key}")
                .putFile(image)
                .addOnCompleteListener {
                    if (it.isSuccessful)
                        trySend(ResultState.Success("Picture uploaded"))
                }.addOnFailureListener {
                    trySend(ResultState.Failure(it))
                }

            awaitClose {
                close()
            }
        }

    override fun getProfilePic(key: String): Flow<ResultState<Uri>> = callbackFlow {
        trySend(ResultState.Loading)

        storage.child("profilePictures")
            .child(key)
            .downloadUrl
            .addOnSuccessListener {
                trySend(ResultState.Success(it))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun updateUser(res: UserDataModelResponse.User): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.collection("user")
            .document(res.userId!!)
            .set(res)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    trySend(ResultState.Success("User Updated"))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }
}