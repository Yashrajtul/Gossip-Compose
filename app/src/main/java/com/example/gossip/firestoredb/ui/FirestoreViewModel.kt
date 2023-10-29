package com.example.gossip.firestoredb.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gossip.firestoredb.UserDataModelResponse
import com.example.gossip.firestoredb.repository.FirestoreRepository
import com.example.gossip.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirestoreViewModel @Inject constructor(
    private val repo: FirestoreRepository
) : ViewModel() {

    private val _res: MutableState<FirestoreState> = mutableStateOf(FirestoreState())
    val res: State<FirestoreState> = _res
    fun insertUser(user: UserDataModelResponse) = repo.insertUser(user)

    init {
        getUsers()
    }

    fun getUsers() = viewModelScope.launch {
        repo.getUsers().collect{
            when(it){
                is ResultState.Success->{
                    _res.value = FirestoreState(
                        data = it.data
                    )
                }
                is ResultState.Failure->{
                    _res.value = FirestoreState(
                        error = it.msg.toString()
                    )
                }
                ResultState.Loading->{
                    _res.value = FirestoreState(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun delete(key:String) = repo.delete(key)
    fun updateUser(user: UserDataModelResponse) = repo.updateUser(user)
}

data class FirestoreState(
    val data: List<UserDataModelResponse> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)