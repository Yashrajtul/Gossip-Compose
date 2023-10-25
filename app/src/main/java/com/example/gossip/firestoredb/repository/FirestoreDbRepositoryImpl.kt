package com.example.gossip.firestoredb.repository

import com.example.gossip.firestoredb.UserDataModelResponse
import com.example.gossip.utils.ResultState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirestoreDbRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : FirestoreRepository {
    override fun insertUser(user: UserDataModelResponse): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            db.collection("user")
                .document(user.key!!)
                .set(user.user!!)
                .addOnSuccessListener {
                    trySend(ResultState.Success("Data is inserted with ${user.key}"))
                }.addOnFailureListener {
                    trySend(ResultState.Failure(it))
                }

            awaitClose {
                close()
            }
        }

    override fun getUser(): Flow<ResultState<List<UserDataModelResponse>>> = callbackFlow {
        trySend(ResultState.Loading)

        db.collection("user")
            .get()
            .addOnSuccessListener {
                val items = it.map { data ->
                    UserDataModelResponse(
                        user = UserDataModelResponse.User(
                            first = data["first"] as String?,
                            last = data["last"] as String?,
                            phone = data["phone"] as String?,
                            profilepicture = data["profilepicture"] as String?
                        ),
                        key = data.id
                    )
                }
                trySend(ResultState.Success(items))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
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

    override fun updateUser(res: UserDataModelResponse): Flow<ResultState<String>> = callbackFlow {
        val map = HashMap<String, Any>()
        map["first"] = res.user?.first!!
        map["last"] = res.user.last!!
        map["phone"] = res.user.phone!!
        map["profilepicture"] = res.user.profilepicture!!

        db.collection("user")
            .document(res.key!!)
            .update(map)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    trySend(ResultState.Success("Updated Successfully..."))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }
}