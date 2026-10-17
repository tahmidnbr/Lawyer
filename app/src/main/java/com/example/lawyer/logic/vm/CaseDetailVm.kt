package com.example.lawyer.logic.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lawyer.logic.repo.CaseRepository
import com.example.lawyer.presentation.UiEvent
import com.example.lawyer.presentation.model.CaseWithHearings
import com.example.lawyer.presentation.model.Hearing
import com.example.lawyer.presentation.model.LegalCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CaseDetailUiState(
    val caseWithHearings: CaseWithHearings? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    val case: LegalCase? get() = caseWithHearings?.case
    val hearings: List<Hearing> get() = caseWithHearings?.hearingsSorted ?: emptyList()
    val nextHearing: Hearing?   get() = caseWithHearings?.nextHearing
}

class CaseDetailVm(
    private val repo: CaseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val caseId: Long = savedStateHandle.get<String>("caseId")
        ?.toLongOrNull() ?: error("caseId required")

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events: Flow<UiEvent> = _events.receiveAsFlow()

    val uiState: StateFlow<CaseDetailUiState> =
        repo.getCaseWithHearings(caseId)
            .map { CaseDetailUiState(caseWithHearings = it, isLoading = false) }
            .catch { e -> emit(CaseDetailUiState(isLoading = false, errorMessage = e.message)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CaseDetailUiState(isLoading = true)
            )

    fun togglePriority() {
        val case = uiState.value.case ?: return
        viewModelScope.launch {
            runCatching { repo.updateCase(case.copy(isPriority = !case.isPriority)) }
                .onFailure { _events.send(UiEvent.ShowToast("Failed to update priority")) }
        }
    }

    fun deleteCase() {
        viewModelScope.launch {
            runCatching { repo.deleteCase(caseId) }
                .onSuccess  { _events.send(UiEvent.NavigateBack) }
                .onFailure  { _events.send(UiEvent.ShowToast("Failed to delete case")) }
        }
    }
}