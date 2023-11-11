package com.example.gossip.ui.home

import androidx.lifecycle.ViewModel
import com.example.gossip.firestoredb.repository.FirestoreRepository
import com.example.gossip.model.UserDataModelResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val fstoreRepo: FirestoreRepository
) : ViewModel() {
    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()
}

data class SearchState(
    val users: List<UserDataModelResponse.User?> = emptyList()
)