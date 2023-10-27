package com.example.gossip.firebaseauth.ui

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gossip.firebaseauth.repository.AuthRepository
import com.example.gossip.firestoredb.UserDataModelResponse
import com.example.gossip.firestoredb.repository.FirestoreRepository
import com.example.gossip.utils.ResultState
import com.example.gossip.utils.showMsg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val fstoreRepo: FirestoreRepository
) : ViewModel() {
    private val _authUiState = MutableStateFlow(AuthState())
    val authUiState: StateFlow<AuthState> = _authUiState.asStateFlow()

    var isDialog by mutableStateOf(false)
        private set
    private var phone by mutableStateOf("")
    private fun createUserWithPhone(
        phone: String,
        activity: Activity
    ) = authRepo.createUserWithPhone(phone, activity)

    private fun signInWithCredential(
        otp: String
    ) = authRepo.signWithCredential(otp)

    private fun insertUser(user: UserDataModelResponse) = fstoreRepo.insertUser(user)

    fun sendOtp(
        mobile: String,
        activity: Activity
    ) {
        phone = mobile
        viewModelScope.launch {
            createUserWithPhone(
                mobile,
                activity
            ).collect {
                when (it) {
                    is ResultState.Success -> {
                        isDialog = false
                        activity.showMsg(it.data)
                    }

                    is ResultState.Failure -> {
                        isDialog = false
                        activity.showMsg(it.msg.toString())
                    }

                    ResultState.Loading -> {
                        isDialog = true
                    }
                }
            }
        }
    }

    fun verifyOtp(
        otp: String,
        activity: Activity
    ) {
        viewModelScope.launch {
            signInWithCredential(
                otp
            ).collect {
                when (it) {
                    is ResultState.Success -> {
                        isDialog = false
                        insertUser(
                            user = UserDataModelResponse(
                                user = UserDataModelResponse.User(
                                    phone = phone
                                ),
                                key = authRepo.currentUser()
                            )
                        ).collect {
                            when (it) {
                                is ResultState.Success -> {
                                    isDialog = false
                                    activity.showMsg(it.data)
                                }

                                is ResultState.Failure -> {
                                    isDialog = false
                                    activity.showMsg(it.msg.toString())
                                }

                                ResultState.Loading -> {
                                    isDialog = true
                                }
                            }
                        }
                        activity.showMsg(it.data)
                    }

                    is ResultState.Failure -> {
                        isDialog = false
                        activity.showMsg(it.msg.toString())
                    }

                    ResultState.Loading -> {
                        isDialog = true
                    }
                }
            }
        }
    }
}

data class AuthState(
    val isDialog: Boolean = false,
    val phone: String = "",
    val otp: String = ""
)