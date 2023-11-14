package com.example.gossip.ui.phonelogin

import android.app.Activity
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gossip.firebaseauth.repository.AuthRepository
import com.example.gossip.model.UserDataModelResponse
import com.example.gossip.firestoredb.repository.FirestoreRepository
import com.example.gossip.utils.ResultState
import com.example.gossip.utils.showMsg
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
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

    private var userId: String by mutableStateOf("")


    fun getPhoneNumber(phoneNumber: String) {
        _loginUiState.update {
            it.copy(
                phoneNumber = phoneNumber,
                isButtonEnabled = phoneNumber.isNotEmpty()
            )
        }
    }

    fun getOtp(otp: String) {
        _loginUiState.update { it.copy(otp = otp, isError = false) }
    }

    fun updateNavigationState(){
        _loginUiState.update { it.copy(navigate = false) }
    }

    private fun checkError() {
        _loginUiState.update { it.copy(isError = loginUiState.value.phoneNumber.length != 10) }
    }
    fun login(activity: Activity){
        _loginUiState.update { it.copy(resend = false) }
        sendOtp(activity)
    }
    fun resendOtp(activity: Activity){
        _loginUiState.update {
            it.copy(isButtonEnabled = false, timer = 60L, resend = true)
        }
        sendOtp(activity)
    }

    private fun sendOtp(
        activity: Activity
    ) {
        if (!loginUiState.value.resend) checkError()
        if (loginUiState.value.isError) activity.showMsg("Enter 10 digit number")
        else {
            viewModelScope.launch {
                authRepo.createUserWithPhone(
                    loginUiState.value.phoneNumber,
                    activity,
                    loginUiState.value.resend
                ).collect { it ->
                    when (it) {
                        is ResultState.Success -> {
                            _loginUiState.update { it.copy(isDialog = false, navigate = !loginUiState.value.resend) }
                            activity.showMsg(it.data)
                        }

                        is ResultState.Failure -> {
                            _loginUiState.update { it.copy(isDialog = false, isButtonEnabled = true) }
                            activity.showMsg(it.msg.toString())
                        }

                        ResultState.Loading -> {
                            _loginUiState.update { it.copy(isDialog = true) }
                        }
                    }
                }
            }
        }
    }

    fun verifyOtp(
        activity: Activity
    ) {
        viewModelScope.launch {
            authRepo.signWithCredential(
                loginUiState.value.otp
            ).collect { it ->
                when (it) {
                    is ResultState.Success -> {
//                        _loginUiState.update { it.copy(navigate = true, isError = false) }
                        userId = authRepo.currentUser()

                        getUserData(activity)
                        _loginUiState.update { it.copy(isError = false, isDialog = false) }

                        activity.showMsg(it.data)
                    }

                    is ResultState.Failure -> {
                        _loginUiState.update { it.copy(isDialog = false, isError = true) }
                        activity.showMsg("Invalid OTP")
                    }

                    ResultState.Loading -> {
                        _loginUiState.update { it.copy(isDialog = true) }
                    }
                }
            }
        }
    }

    private fun getUserData(activity: Activity) {
        viewModelScope.launch {
            fstoreRepo.getUserData(userId)
                .collect { user ->
                    when (user) {
                        is ResultState.Success -> {
                            if(user.data != null) {
                                _loginUiState.update { it.copy(username = user.data.username!!, phoneNumber = user.data.phone!!, navigate = true, isDialog = false) }
                            }else{
                                updateUser(UserDataModelResponse.User(
                                    username = loginUiState.value.username,
                                    phone = loginUiState.value.phoneNumber,
                                    userId = userId,
                                    createdTimestamp = Timestamp.now()
                                ), activity)
                                _loginUiState.update { it.copy(isDialog = false)}
                            }
                        }
                        is ResultState.Failure -> { _loginUiState.update { it.copy(isDialog = false) } }
                        ResultState.Loading -> { _loginUiState.update { it.copy(isDialog = true) } }
                    }
                }
        }
    }

    private fun updateUser(user: UserDataModelResponse.User, activity: Activity) {
        viewModelScope.launch {
            fstoreRepo.updateUser(user)
                .collect { it ->
                    when (it) {
                        is ResultState.Success -> {
                            _loginUiState.update { it.copy(isDialog = false, navigate = true) }
//                            activity.showMsg(it.data)
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

//    private fun updateProfilePicture(image: Uri) {
//        viewModelScope.launch {
//            fstoreRepo.uploadPic(image, userId)
//                .collect {it->
//                    when (it) {
//                        is ResultState.Success -> { _loginUiState.update { it.copy(isDialog = false) }}
//                        is ResultState.Failure -> { _loginUiState.update { it.copy(isDialog = false) }}
//                        ResultState.Loading -> { _loginUiState.update { it.copy(isDialog = true) }}
//                    }
//                }
//        }
//    }

//    fun updateProfile(activity: Activity) {
//        _loginUiState.update {
//            it.copy(isError = loginUiState.value.username.length < 3)
//        }
//        if (loginUiState.value.isError) {
//            activity.showMsg("Username length should be at least 3 chars")
//            return
//        }
//        if(loginUiState.value.phoneNumber == ""){
//            activity.showMsg("No phoneNumber")
//            return
//        }
//
//        getUserData(activity)
//        val user = UserDataModelResponse.User(
//                username = loginUiState.value.username,
//                phone = loginUiState.value.phoneNumber,
//                userId = userId,
//                createdTimestamp = Timestamp.now()
//            )
//        if(loginUiState.value.image != null)
//            updateProfilePicture(loginUiState.value.image!!)
//        updateUser(user, activity)
//    }

    fun updateTimer(timer: Long){
        if (timer != 0L)
            _loginUiState.update { it.copy(isButtonEnabled = false, timer = timer) }
        else
            _loginUiState.update { it.copy(isButtonEnabled = true, timer = timer) }
    }

}

data class LoginState(
    var phoneNumber: String = "",
    var otp: String = "",
    var username: String = "",
    var isDialog: Boolean = false,
    var isButtonEnabled: Boolean = false,
    var isError: Boolean = false,
    var resend: Boolean = false,
    var navigate: Boolean = false,
//    var isLoggedIn: Boolean = false,
    var image: Uri? = null,
    var timer: Long = 60L
)