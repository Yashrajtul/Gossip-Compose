package com.example.gossip.ui
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.gossip.model.User
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.stateIn
//
//class ChatViewModel : ViewModel() {
////    private val _uiState = MutableStateFlow(ChatUiState())
////    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
//    private val isLoggedIn = MutableStateFlow(true)
//    private val chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
//    private val users = MutableStateFlow<List<User>>(emptyList())
//
//    val chatState = combine(isLoggedIn, chatMessages, users){ isLoggedIn, messages, users->
//        if(isLoggedIn){
//            ChatState(
//                userPreviews =  users.map {curUser->
//                    UserPreview(
//                        user = curUser,
//                        lastMessage = messages
//                            .filter { it.user == curUser }
//                            .maxByOrNull { it.time }
//                            ?.message
//                    )
//                },
//                headerTitle = users.firstOrNull()?.name ?: "Chat"
//            )
//        } else null
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
//}
//
//data class ChatState(
//    val userPreviews: List<UserPreview>,
//    val headerTitle: String
//)
//data class UserPreview(
//    val user: User,
//    val lastMessage: String?
//)
//data class ChatMessage(
//    val user: User,
//    val message: String,
//    val time: Long
//)