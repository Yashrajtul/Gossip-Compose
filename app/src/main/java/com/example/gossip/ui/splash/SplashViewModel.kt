package com.example.gossip.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gossip.firebaseauth.repository.AuthRepository
import com.example.gossip.firestoredb.repository.FirestoreRepository
import com.example.gossip.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val fstore: FirestoreRepository
) : ViewModel() {
    private val userId = auth.currentUser()
    val isLoggedIn: Boolean = userId != ""
    var usernameEntered: Boolean = false

    init {
        if (isLoggedIn)
            viewModelScope.launch {
                fstore.getUserData(userId).collect {
                    when (it) {
                        is ResultState.Success -> {
                            usernameEntered = it.data?.username != ""
                        }

                        else -> {}
                    }
                }

            }

    }
}