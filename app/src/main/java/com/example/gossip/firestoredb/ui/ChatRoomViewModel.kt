package com.example.gossip.firestoredb.ui

import androidx.lifecycle.ViewModel
import com.example.gossip.model.ChatRoom
import com.example.gossip.firestoredb.repository.FirestoreRepository
import javax.inject.Inject

class ChatRoomViewModel @Inject constructor(
    private val repo: FirestoreRepository
) : ViewModel(){

    fun createChatRoom(chat: ChatRoom.Chat) = repo.createChatRoom(chat)

}