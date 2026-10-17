package com.example.lawyer.logic.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lawyer.logic.repo.CaseRepository
import com.example.lawyer.presentation.UiEvent
import com.example.lawyer.presentation.model.CaseWithHearings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class CaseSort { ALL, PRIORITY }

data class CaseListUiState(
    val cases: List<CaseWithHearings> = emptyList(),
    val activeSort: CaseSort = CaseSort.ALL,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class CaseListVm(private val repo: CaseRepository) : ViewModel() {

    private val _sort = MutableStateFlow(CaseSort.ALL)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CaseListUiState> =
        _sort.flatMapLatest { sort ->
            when (sort) {
                CaseSort.ALL      -> repo.getAllCasesWithHearings()
                CaseSort.PRIORITY -> repo.getPriorityCasesWithHearings()
            }
        }
            .map { CaseListUiState(cases = it, activeSort = _sort.value, isLoading = false) }
            .catch { e -> emit(CaseListUiState(isLoading = false, errorMessage = e.message)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CaseListUiState(isLoading = true)
            )

    fun setSort(sort: CaseSort) { _sort.value = sort }

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events: Flow<UiEvent> = _events.receiveAsFlow()

    fun deleteCase(caseId: Long) {
        viewModelScope.launch {
            runCatching { repo.deleteCase(caseId) }
                .onFailure { _events.send(UiEvent.ShowToast("Failed to delete case")) }
        }
    }
}