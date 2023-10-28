package com.example.gossip.ui.phonelogin

import android.app.Activity
import androidx.compose.runtime.State
import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val fstoreRepo: FirestoreRepository
) : ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginState())
    val loginUiState: StateFlow<LoginState> = _loginUiState.asStateFlow()
//    var loginUiState = savedStateHandle.getStateFlow("login", LoginState())
//        private set
    fun getPhoneNumber(phoneNumber: String) {
        _loginUiState.update {
            it.copy(
                phoneNumber = phoneNumber,
                isButtonEnabled = phoneNumber.isNotEmpty()
            )
        }
    }

    fun checkError() {
        _loginUiState.update {
            it.copy(
                isError = loginUiState.value.phoneNumber.length != 10
            )
        }
    }

    fun sendOtp(
//        mobile: String,
        activity: Activity
    ) {
        checkError()
        if (!loginUiState.value.isError) {
//        phone = mobile
            viewModelScope.launch {
                authRepo.createUserWithPhone(
                    loginUiState.value.phoneNumber,
                    activity
                ).collect {
                    when (it) {
                        is ResultState.Success -> {
                            _loginUiState.update { it.copy(isDialog = false) }
//                        isDialog = false
                            activity.showMsg(it.data)
                        }

                        is ResultState.Failure -> {
                            _loginUiState.update { it.copy(isDialog = false) }
//                        isDialog = false
                            activity.showMsg(it.msg.toString())
                        }

                        ResultState.Loading -> {
                            _loginUiState.update { it.copy(isDialog = true) }
//                        isDialog = true
                        }
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
            authRepo.signWithCredential(
                otp
            ).collect {
                when (it) {
                    is ResultState.Success -> {
                        _loginUiState.update { it.copy(isDialog = false) }
//                        isDialog = false
                        fstoreRepo.insertUser(
                            user = UserDataModelResponse(
                                user = UserDataModelResponse.User(
                                    phone = loginUiState.value.phoneNumber
//                                    phone = phone
                                ),
                                key = authRepo.currentUser()
                            )
                        ).collect {
                            when (it) {
                                is ResultState.Success -> {
                                    _loginUiState.update { it.copy(isDialog = false) }
//                                    isDialog = false
                                    activity.showMsg(it.data)
                                }

                                is ResultState.Failure -> {
                                    _loginUiState.update { it.copy(isDialog = false) }
//                                    isDialog = false
                                    activity.showMsg(it.msg.toString())
                                }

                                ResultState.Loading -> {
                                    _loginUiState.update { it.copy(isDialog = true) }
//                                    isDialog = true
                                }
                            }
                        }
                        activity.showMsg(it.data)
                    }

                    is ResultState.Failure -> {
                        _loginUiState.update { it.copy(isDialog = false) }
//                        isDialog = false
                        activity.showMsg(it.msg.toString())
                    }

                    ResultState.Loading -> {
                        _loginUiState.update { it.copy(isDialog = true) }
//                        isDialog = true
                    }
                }
            }
        }
    }
}

data class LoginState(
    var phoneNumber: String = "",
    var otp: String = "",
    var isDialog: Boolean = false,
    var username: String = "",
    var isButtonEnabled: Boolean = false,
    var isError: Boolean = false
)