package com.example.gossip.data

import com.example.gossip.R
import com.example.gossip.model.Chat
import com.example.gossip.model.Message
import com.example.gossip.model.User
import java.util.Date

object Datasource {
    var chats = listOf<Chat>(
        Chat("Yash", androidx.core.R.drawable.ic_call_answer),
        Chat("Aastha", androidx.core.R.drawable.ic_call_answer_video)
    )
    var messages = listOf<Message>(
        Message("1", "Hi", Date()),
        Message("2", "Hello", Date())
    )
    var users = listOf<User>(
        User("1", "yash@email.com", "Yash", ""),
        User("2", "aastha@email.com", "Aastha", "")
    )

}