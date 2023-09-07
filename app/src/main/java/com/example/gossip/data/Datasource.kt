package com.example.gossip.data

import com.example.gossip.R
import com.example.gossip.model.Chat

object Datasource {
    var chats = listOf<Chat>(
        Chat("Yash", androidx.core.R.drawable.ic_call_answer),
        Chat("Aastha", androidx.core.R.drawable.ic_call_answer_video)
    )
}