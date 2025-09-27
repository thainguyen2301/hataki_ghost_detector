package com.hataki.ghostdetector.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String?>()
}

