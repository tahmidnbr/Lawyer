package com.example.lawyer.presentation

sealed class UiState<out T> {
    data object Idle    : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class  Success<T>(val data: T) : UiState<T>()
    data class  Error(val message: String) : UiState<Nothing>()
}

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data class NavigateTo(val route: String)  : UiEvent()
    data object NavigateBack                  : UiEvent()
}