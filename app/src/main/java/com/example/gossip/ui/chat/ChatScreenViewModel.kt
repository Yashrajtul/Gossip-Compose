package com.example.gossip.ui.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gossip.data.remote.ChatSocketService
import com.example.gossip.data.remote.MessageService
import com.example.gossip.firebaseauth.repository.AuthRepository
import com.example.gossip.firestoredb.repository.ChatRepository
import com.example.gossip.model.ChatMessageModel
import com.example.gossip.model.ChatRoomModel
import com.example.gossip.model.MessageRegister
import com.example.gossip.model.MessageRegister1
import com.example.gossip.model.MessageStatus
import com.example.gossip.model.MessageType
import com.example.gossip.util.Resource
import com.example.gossip.utils.ResultState
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val chatRepo: ChatRepository,
    private val messageService: MessageService,
    private val chatSocketService: ChatSocketService
) : ViewModel() {
    private val _chatState = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    private val myUserId: String = auth.currentUser()
    private var opponentUserId: String = ""

    private var room by mutableStateOf(ChatRoomModel.ChatRoom())

    fun onMessageChange(input: TextFieldValue) {
        _chatState.update { it.copy(messageInput = input) }
    }

    fun getChatRoom(userId: String) {
        opponentUserId = userId
        getOrCreateChatRoom()
    }

    private fun getChatRoomId(userId1: String, userId2: String): String {
        return if (userId1.hashCode() < userId2.hashCode()) userId1 + "_" + userId2
        else userId2 + "_" + userId1
    }

    private fun getOrCreateChatRoom() {
        val chatRoomId = getChatRoomId(myUserId, opponentUserId)
        viewModelScope.launch {
            val chatRoom = chatRepo.getChatRoom(chatRoomId)
            if (chatRoom == null) {
                room = ChatRoomModel.ChatRoom(
                    members = arrayListOf(myUserId, opponentUserId),
                    chatRoomId = chatRoomId,
                    lastUpdated = Timestamp.now()
                )
                val result = chatRepo.updateChatRoom(room)
                when (result) {
                    is Resource.Success -> {
                        _toastEvent.emit("ChatRoom Created")
                        getUserData()
                    }

                    is Resource.Error -> {
                        _toastEvent.emit(result.message ?: "Unknown error")
                    }
                }
            } else {
                room = chatRoom
                getUserData()
            }
        }
    }

    fun getUserData() {
        viewModelScope.launch {
            val user = chatRepo.getUserData(opponentUserId)
            _chatState.update { it.copy(name = user?.username!!) }
//            updateMessages()
        }
        getAllMessages()
        viewModelScope.launch {
            val result = chatSocketService.initSession(myUserId, room.chatRoomId)
            when(result){
                is Resource.Success->{
                    chatSocketService.observeMessage()
                        .onEach { message ->
                            val newList = chatState.value.messages1.toMutableList().apply {
                                add(0, MessageRegister1(message, message.userId==myUserId))
                            }
                            _chatState.update { it.copy(messages1 = newList) }
                        }.launchIn(viewModelScope)
                }
                is Resource.Error->{
                    _toastEvent.emit(result.message ?: "Unknown error")
                }
            }
        }
    }

//    fun sendMessage() {
//        val message = chatState.value.messageInput.text.trim()
//        viewModelScope.launch {
////            _chatState.update {
////                it.copy(
////                    chatRoom = ChatRoomModel.ChatRoom(
////                        members = _chatState.value.chatRoom.members,
////                        chatRoomId = _chatState.value.chatRoom.chatRoomId,
////                        lastMessageSenderId = myUserId,
////                        lastUpdated = Timestamp.now()
////                    )
////                )
////            }
//            room = ChatRoomModel.ChatRoom(
//                members = room.members,
//                chatRoomId = room.chatRoomId,
//                lastMessageSenderId = myUserId,
//                lastMessage = message,
//                lastUpdated = Timestamp.now()
//            )
////            chatRepo.updateChatRoom(chatState.value.chatRoom)
//            val result = chatRepo.updateChatRoom(room)
//            when (result) {
//                is Resource.Success -> {
//                    val chat = ChatMessageModel.Message(
//                        senderId = myUserId,
//                        type = MessageType.TEXT.name,
//                        messageContent = message,
//                        status = MessageStatus.RECEIVED.name,
//                        timestamp = room.lastUpdated
//                    )
//                    val result = chatRepo.sendMessage(room.chatRoomId, chat)
//                    when (result) {
//                        is Resource.Success -> {
//                            _chatState.update { it.copy(messageInput = TextFieldValue()) }
//                            updateMessages()
//                        }
//
//                        is Resource.Error -> {}
//                    }
//                }
//
//                is Resource.Error -> {
//                    _toastEvent.emit(result.message ?: "Unknown error")
//                }
//            }
//
//        }
//    }

//    private fun updateMessages() {
//        viewModelScope.launch {
//            chatRepo.getMessages(room.chatRoomId)
//                .collect { messages ->
//                    when (messages) {
//                        is ResultState.Success -> {
//                            _chatState.update {
//                                it.copy(
//                                    messages = messages.data.map { message ->
//                                        MessageRegister(
//                                            chatMessage = message!!,
//                                            isMessageFromOpponent = message.senderId != myUserId
//                                        )
//                                    }
//                                )
//                            }
//                        }
//
//                        is ResultState.Failure -> {}
//                        ResultState.Loading -> {}
//                    }
//                }
//        }
//    }

    fun sendMessage1() {
        viewModelScope.launch {
            if (chatState.value.messageInput.text.isNotBlank()) {
                val message = chatState.value.messageInput.text.trim()
                chatSocketService.sendMessage(message)
                _chatState.update { it.copy(messageInput = TextFieldValue()) }
                room = ChatRoomModel.ChatRoom(
                    members = room.members,
                    chatRoomId = room.chatRoomId,
                    lastMessageSenderId = myUserId,
                    lastMessage = message,
                    lastUpdated = Timestamp.now()
                )
                chatRepo.updateChatRoom(room)
            }
        }
    }

    private fun getAllMessages(){
        viewModelScope.launch {
            _chatState.update { it.copy(isLoading = true) }
            val result = messageService.getAllMessage(room.chatRoomId)
            _chatState.update { it.copy(
                messages1 = result.map { message ->
                    MessageRegister1(
                        chatMessage = message,
                        isMyMessage = message.userId == myUserId
                    )
                },
                isLoading = false
            ) }
        }
    }
    fun disconnect() {
        viewModelScope.launch {
            chatSocketService.closeSession()
        }
    }
    override fun onCleared() {
        super.onCleared()
        disconnect()
    }

}

data class ChatState(
    val name: String = "",
    val profilePic: String = "",
    val messageInput: TextFieldValue = TextFieldValue(""),
    val isLoading: Boolean = false,
//    val chatRoom: ChatRoomModel.ChatRoom = ChatRoomModel.ChatRoom(),
    val messages: List<MessageRegister> = emptyList(),
    val messages1: List<MessageRegister1> = emptyList()
)