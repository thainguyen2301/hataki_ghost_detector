package com.hataki.ghostdetector.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hataki.ghostdetector.data.model.User
import com.hataki.ghostdetector.data.repository.UserRepository
import com.hataki.ghostdetector.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    val users = MutableLiveData<List<User>>()

    fun fetchUsers() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                users.value = userRepository.getUsers()
            } catch (e: Exception) {
                errorMessage.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }
}
