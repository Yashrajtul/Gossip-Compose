package com.example.gossip.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gossip.firebaseauth.repository.AuthRepository
import com.example.gossip.firestoredb.repository.HomeRepository
import com.example.gossip.model.UserDataModelResponse
import com.example.gossip.util.Resource
import com.example.gossip.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreensViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val repo: HomeRepository
) : ViewModel() {
    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    val myUserId = auth.currentUser()

    fun getChatRooms(){
        viewModelScope.launch {
            try {
                repo.getChatUsers(myUserId).collect{users->
                    when (users){
                        is ResultState.Success -> {
                            val chatRooms = users.data
                            val chatUsers = chatRooms.map { chatRoom ->
                                val result = repo.getOtherUser(chatRoom.members, myUserId)
                                result.userRoom = chatRoom
                                result
                            }
                            _homeState.update { it.copy(chatUsers = chatUsers, isLoading = false) }
                        }
                        is ResultState.Failure -> { _homeState.update { it.copy(isLoading = false) } }
                        ResultState.Loading -> {_homeState.update { it.copy(isLoading = true) }}
                    }
                }
            }catch (e: Exception){
                e.printStackTrace()
                _homeState.update { it.copy(isLoading = false) }
                _toastEvent.emit(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}

data class HomeState(
    val isLoading: Boolean = false,
    val chatUsers: List<UserDataModelResponse> = emptyList()
)