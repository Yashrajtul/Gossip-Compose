package com.example.gossip.ui.phonelogin

import android.app.Activity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gossip.firebaseauth.repository.AuthRepository
import com.example.gossip.firestoredb.UserDataModelResponse
import com.example.gossip.firestoredb.repository.FirestoreRepository
import com.example.gossip.utils.ResultState
import com.example.gossip.utils.showMsg
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val fstoreRepo: FirestoreRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){
    var loginUiState = savedStateHandle.getStateFlow("login", LoginState())
        private set

    fun getPhoneNumber(phoneNumber: String){
        loginUiState.value.phoneNumber = phoneNumber
        loginUiState.value.isButtonEnabled = phoneNumber.isNotEmpty()
        savedStateHandle["login"] = loginUiState
    }

    fun checkError(){
        loginUiState.value.isError = loginUiState.value.phoneNumber.length != 10
        savedStateHandle["login"] = loginUiState
    }
    fun sendOtp(
//        mobile: String,
        activity: Activity
    ) {
        checkError()
        if(!loginUiState.value.isError){
//        phone = mobile
            viewModelScope.launch {
                authRepo.createUserWithPhone(
                    loginUiState.value.phoneNumber,
                    activity
                ).collect {
                    when (it) {
                        is ResultState.Success -> {
                            loginUiState.value.isDialog = false
//                        isDialog = false
                            activity.showMsg(it.data)
                        }

                        is ResultState.Failure -> {
                            loginUiState.value.isDialog = false
//                        isDialog = false
                            activity.showMsg(it.msg.toString())
                        }

                        ResultState.Loading -> {
                            loginUiState.value.isDialog = true
//                        isDialog = true
                        }
                    }
                    savedStateHandle["login"] = loginUiState
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
                        loginUiState.value.isDialog = false
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
                                    loginUiState.value.isDialog = false
//                                    isDialog = false
                                    activity.showMsg(it.data)
                                }

                                is ResultState.Failure -> {
                                    loginUiState.value.isDialog = false
//                                    isDialog = false
                                    activity.showMsg(it.msg.toString())
                                }

                                ResultState.Loading -> {
                                    loginUiState.value.isDialog = true
//                                    isDialog = true
                                }
                            }
                        }
                        activity.showMsg(it.data)
                    }

                    is ResultState.Failure -> {
                        loginUiState.value.isDialog = false
//                        isDialog = false
                        activity.showMsg(it.msg.toString())
                    }

                    ResultState.Loading -> {
                        loginUiState.value.isDialog = true
//                        isDialog = true
                    }
                }
                savedStateHandle["login"] = loginUiState
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