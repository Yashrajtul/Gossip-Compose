package com.example.gossip.model

import android.net.Uri
import com.google.firebase.Timestamp

data class UserDataModelResponse(
    val user: User?,
    val profilePic: Uri? = null,
    val key: String? = "",
    var userRoom: ChatRoomModel.ChatRoom? = null
){
    data class User(
        val username: String? = "",
        val phone: String? = "",
        val userId: String? = "",
        val createdTimestamp: Timestamp? = null
    ){
        fun doesMatchSearchQuery(query: String): Boolean{
            val matchingCombinations = listOf(
                "$username$phone",
                "$username $phone"
            )

            return matchingCombinations.any{
                it.contains(query, ignoreCase = true)
            }
        }
    }
}
