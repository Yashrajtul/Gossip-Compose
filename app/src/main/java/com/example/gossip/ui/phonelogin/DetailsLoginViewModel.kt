package com.example.gossip.ui.phonelogin

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gossip.firebaseauth.repository.AuthRepository
import com.example.gossip.firestoredb.repository.FirestoreRepository
import com.example.gossip.model.UserDataModelResponse
import com.example.gossip.utils.ResultState
import com.example.gossip.utils.showMsg
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsLoginViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val fstoreRepo: FirestoreRepository
) : ViewModel(){
    private val _detailsState = MutableStateFlow(DetailsState())
    val detailsState: StateFlow<DetailsState> = _detailsState.asStateFlow()

    init {
        _detailsState.update { it.copy(userId = authRepo.currentUser()) }
    }

    fun getUserName(username: String) {
        _detailsState.update { it.copy(username = username) }
    }

    fun getImage(image: Uri?) {
        _detailsState.update { it.copy(image = image) }
    }

    fun getUserData(activity: Activity) {
        viewModelScope.launch {
            fstoreRepo.getUserData(detailsState.value.userId)
                .collect { user ->
                    when (user) {
                        is ResultState.Success -> {
                            if(user.data != null) {
                                _detailsState.update { it.copy(username = user.data.username!!, phoneNumber = user.data.phone!!) }
                            }
                            _detailsState.update { it.copy( isDialog = false ) }
                        }
                        is ResultState.Failure -> { _detailsState.update { it.copy(isDialog = false) } }
                        ResultState.Loading -> { _detailsState.update { it.copy(isDialog = true) } }
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
                            _detailsState.update { it.copy(isDialog = false, navigate = true) }
                            activity.showMsg(it.data)
                        }

                        is ResultState.Failure -> {
                            _detailsState.update { it.copy(isDialog = false) }
                            activity.showMsg(it.toString())
                        }

                        ResultState.Loading -> {
                            _detailsState.update { it.copy(isDialog = true) }
                        }
                    }
                }
        }
    }

    private fun updateProfilePicture(image: Uri) {
        viewModelScope.launch {
            fstoreRepo.uploadPic(image, detailsState.value.userId)
                .collect {it->
                    when (it) {
                        is ResultState.Success -> { _detailsState.update { it.copy(isDialog = false) }}
                        is ResultState.Failure -> { _detailsState.update { it.copy(isDialog = false) }}
                        ResultState.Loading -> { _detailsState.update { it.copy(isDialog = true) }}
                    }
                }
        }
    }

    fun updateProfile(activity: Activity) {
        _detailsState.update {
            it.copy(isError = detailsState.value.username.length < 3)
        }
        if (detailsState.value.isError) {
            activity.showMsg("Username length should be at least 3 chars")
            return
        }
        if(detailsState.value.phoneNumber == ""){
            activity.showMsg("No phoneNumber")
            return
        }

        getUserData(activity)
        val user = UserDataModelResponse.User(
            username = detailsState.value.username,
            phone = detailsState.value.phoneNumber,
            userId = detailsState.value.userId,
            createdTimestamp = Timestamp.now()
        )
        if(detailsState.value.image != null)
            updateProfilePicture(detailsState.value.image!!)
        updateUser(user, activity)
    }
}

data class DetailsState(
    val username: String = "",
    var phoneNumber: String = "",
    var userId: String = "",
    var image: Uri? = null,
    var navigate: Boolean = false,
    var isDialog: Boolean = false,
    var isError: Boolean = false

    )