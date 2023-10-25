package com.example.gossip.firestoredb

data class UserDataModelResponse(
    val user: User?,
    val key: String? = ""
){
    data class User(
        val first: String? = "",
        val last: String? = "",
        val phone: String? = "",
        val profilepicture: String? = ""
    )
}
