package com.example.gossip.firestoredb

data class FirestoreModelResponse(
    val user: User?,
    val key: String?
){
    data class User(
        val first: String? = "",
        val last: String? = "",
        val image: String? = ""
    )
}
