package com.example.gossip.model

import com.google.firebase.Timestamp

data class UserDataModelResponse(
    val user: User?,
    val key: String? = ""
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
