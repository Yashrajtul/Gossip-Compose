package com.example.gossip.ui.phonelogin

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gossip.firebaseauth.repository.AuthRepository
import com.example.gossip.firestoredb.UserDataModelResponse
import com.example.gossip.firestoredb.repository.FirestoreRepository
import com.example.gossip.utils.ResultState
import com.example.gossip.utils.showMsg
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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

    fun getPhoneNumber(phoneNumber: String) {
        _loginUiState.update {
            it.copy(
                phoneNumber = phoneNumber,
                isButtonEnabled = phoneNumber.isNotEmpty()
            )
        }
    }

    fun getOtp(otp: String) {
        _loginUiState.update {
            it.copy(
                otp = otp,
                isButtonEnabled = otp.isNotEmpty()
            )
        }
    }

    fun getUserName(username: String) {
        _loginUiState.update {
            it.copy(
                username = username
            )
        }
    }

    fun getImage(image: Uri?) {
        _loginUiState.update {
            it.copy(
                image = image
            )
        }
    }

    private fun checkError() {
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
                            _loginUiState.update {
                                it.copy(
                                    isDialog = false,
//                                    otpSent = true
                                )
                            }
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
//        otp: String,
        activity: Activity
    ) {
        viewModelScope.launch {
            authRepo.signWithCredential(
                loginUiState.value.otp
            ).collect {
                when (it) {
                    is ResultState.Success -> {
                        _loginUiState.update { it.copy(isDialog = false) }
                        val userId = authRepo.currentUser()
                        fstoreRepo.insertUser(
                            user = UserDataModelResponse(
                                user = UserDataModelResponse.User(
                                    phone = loginUiState.value.phoneNumber,
                                    userId = userId
                                ),
                                key = userId
                            )
                        ).collect {
                            when (it) {
                                is ResultState.Success -> {
                                    _loginUiState.update {
                                        it.copy(
                                            isDialog = false,
//                                            otpVerified = true
                                        )
                                    }
                                    activity.showMsg(it.data)
                                }

                                is ResultState.Failure -> {
                                    _loginUiState.update { it.copy(isDialog = false) }
                                    activity.showMsg(it.msg.toString())
                                }

                                ResultState.Loading -> {
                                    _loginUiState.update { it.copy(isDialog = true) }
                                }
                            }
                        }
                        activity.showMsg(it.data)
                    }

                    is ResultState.Failure -> {
                        _loginUiState.update { it.copy(isDialog = false) }
                        activity.showMsg(it.msg.toString())
                    }

                    ResultState.Loading -> {
                        _loginUiState.update { it.copy(isDialog = true) }
                    }
                }
            }
        }
    }

    fun updateProfile(activity: Activity) {
        _loginUiState.update {
            it.copy(isError = loginUiState.value.username.length < 3)
        }
        if (loginUiState.value.isError) {
            activity.showMsg("Username length should be at least 3 chars")
            return
        }
        viewModelScope.launch {
            val userId = authRepo.currentUser()
            val user = UserDataModelResponse(
                user = UserDataModelResponse.User(
                    username = loginUiState.value.username,
                    phone = loginUiState.value.phoneNumber,
                    userId = userId,
                    createdTimestamp = Timestamp.now()
                ),
                key = userId
            )

            if (loginUiState.value.image != null) {
                fstoreRepo.uploadPic(
                    loginUiState.value.image!!,
                    user
                ).collect {
                    when (it) {
                        is ResultState.Success -> {
                            _loginUiState.update { it.copy(isDialog = false) }
                            fstoreRepo.updateUser(user)
                                .collect {
                                    when (it) {
                                        is ResultState.Success -> {
                                            _loginUiState.update { it.copy(isDialog = false) }
                                            activity.showMsg("UserInfo Updated")
                                        }

                                        is ResultState.Failure -> {
                                            _loginUiState.update { it.copy(isDialog = false) }
                                            activity.showMsg(it.toString())
                                        }

                                        ResultState.Loading -> {
                                            _loginUiState.update { it.copy(isDialog = true) }
                                        }
                                    }
                                }
                        }

                        is ResultState.Failure -> {
                            _loginUiState.update { it.copy(isDialog = false) }
                            activity.showMsg(it.toString())
                        }

                        ResultState.Loading -> {
                            _loginUiState.update { it.copy(isDialog = true) }
                        }
                    }
                }
            } else {
                fstoreRepo.updateUser(user)
                    .collect {
                        when (it) {
                            is ResultState.Success -> {
                                _loginUiState.update { it.copy(isDialog = false) }
                                activity.showMsg("UserInfo Updated")
                            }

                            is ResultState.Failure -> {
                                _loginUiState.update { it.copy(isDialog = false) }
                                activity.showMsg(it.toString())
                            }

                            ResultState.Loading -> {
                                _loginUiState.update { it.copy(isDialog = true) }
                            }
                        }
                    }
            }
        }
    }
}

data class LoginState(
    var phoneNumber: String = "",
    var otp: String = "",
    var username: String = "",
    var isDialog: Boolean = false,
    var isButtonEnabled: Boolean = false,
    var isError: Boolean = false,
    var image: Uri? = null
)