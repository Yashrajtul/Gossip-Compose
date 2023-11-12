package com.example.gossip.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gossip.firestoredb.repository.FirestoreRepository
import com.example.gossip.model.UserDataModelResponse
import com.example.gossip.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val fstoreRepo: FirestoreRepository
) : ViewModel() {
    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()

    init {
        viewModelScope.launch {
            fstoreRepo.getUsers()
                .collect { users ->
                    when (users) {
                        is ResultState.Success -> {
                            _searchState.update { it.copy(allUsers = users.data) }
                        }

                        is ResultState.Failure -> {}
                        ResultState.Loading -> {}
                    }
                }
        }
    }
    fun onSearchTextChange(text: String){
        _searchState.update { it.copy(searchText = text) }
        if(text.length>=3) {
            _searchState.update { it.copy(
                users = searchState.value.allUsers.filter {user->
                    user.doesMatchSearchQuery(searchState.value.searchText)
                }) }
        }else _searchState.update { it.copy(users = emptyList()) }
    }

}

data class SearchState(
    val searchText: String = "",
    val users: List<UserDataModelResponse.User> = emptyList(),
    val allUsers: List<UserDataModelResponse.User> = emptyList()
)