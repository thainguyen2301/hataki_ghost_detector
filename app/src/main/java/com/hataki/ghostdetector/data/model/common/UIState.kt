package com.hataki.ghostdetector.data.model.common

sealed class UIState<out T> {
    object Idle : UIState<Nothing>()
    object Loading : UIState<Nothing>()
    data class Success<out T>(val data: T) : UIState<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : UIState<Nothing>()
}

fun <T> UIState<T>.getOrNull(): T? {
    return when (this) {
        is UIState.Success -> this.data
        else -> null
    }
}