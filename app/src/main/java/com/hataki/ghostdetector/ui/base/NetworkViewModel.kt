package com.hataki.ghostdetector.ui.base

import androidx.lifecycle.viewModelScope
import com.hataki.ghostdetector.data.repository.network.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private var networkRepository: NetworkRepository
) : BaseViewModel() {
    private val _isNetworkConnected = MutableStateFlow(true)
    val isNetworkConnected: StateFlow<Boolean> = _isNetworkConnected

    init {
        viewModelScope.launch {
            networkRepository.observeNetwork().collect { status ->
                _isNetworkConnected.value = status
            }
        }
    }
}