package com.example.gossip.firebaseRealtimeDb

import android.media.Image

data class RealtimeModelResponse(
    val user: User?,
    val key: String?

){
    data class User(
        val first: String?,
        val last: String?,
        val image: String?
    )
}
