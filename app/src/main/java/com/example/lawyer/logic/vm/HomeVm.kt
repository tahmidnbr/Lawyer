package com.example.lawyer.logic.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lawyer.logic.repo.CaseRepository
import com.example.lawyer.presentation.model.CaseWithHearings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val priorityCases: List<CaseWithHearings> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class HomeVm(private val repo: CaseRepository): ViewModel() {
    val uiState: StateFlow<HomeUiState> =
        repo.getPriorityCasesWithHearings()
            .map { HomeUiState(priorityCases = it, isLoading = false) }
            .catch { e -> emit(HomeUiState(isLoading = false, errorMessage = e.message)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeUiState(isLoading = true)
            )

}