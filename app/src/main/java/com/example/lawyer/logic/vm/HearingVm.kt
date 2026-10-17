package com.example.lawyer.logic.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lawyer.logic.repo.CaseRepository
import com.example.lawyer.presentation.UiEvent
import com.example.lawyer.presentation.model.CaseWithHearings
import com.example.lawyer.presentation.model.Hearing
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HearingFormState(
    val editingId: Long? = null,
    val purposeOrProceeding: String = "",
    val notes: String = "",
    val hearingDate: Long = System.currentTimeMillis(),
    val isVisible: Boolean = false
) {
    val isValid: Boolean get() = purposeOrProceeding.isNotBlank()
}

data class HearingUiState(
    val caseWithHearings: CaseWithHearings? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    val caseTitle: String   get() = caseWithHearings?.case?.title ?: ""
    val hearings: List<Hearing> get() = caseWithHearings?.hearingsSorted ?: emptyList()
}

class HearingVm(
    private val repo: CaseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val caseId: Long = savedStateHandle.get<String>("caseId")
        ?.toLongOrNull() ?: error("caseId required")

    val uiState: StateFlow<HearingUiState> =
        repo.getCaseWithHearings(caseId)
            .map { HearingUiState(caseWithHearings = it, isLoading = false) }
            .catch { e -> emit(HearingUiState(isLoading = false, errorMessage = e.message)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HearingUiState(isLoading = true)
            )

    private val _formState = MutableStateFlow(HearingFormState())
    val formState: StateFlow<HearingFormState> = _formState.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events: Flow<UiEvent> = _events.receiveAsFlow()

    // ── Form controls ─────────────────────────────────────────
    fun onProceedingChange(v: String) = _formState.update { it.copy(purposeOrProceeding = v) }
    fun onNotesChange(v: String)      = _formState.update { it.copy(notes = v) }
    fun onDateChange(v: Long)         = _formState.update { it.copy(hearingDate = v) }

    fun openAddForm()  { _formState.value = HearingFormState(isVisible = true) }
    fun dismissForm()  { _formState.update { it.copy(isVisible = false) } }

    fun openEditForm(hearing: Hearing) {
        _formState.value = HearingFormState(
            editingId            = hearing.id,
            purposeOrProceeding  = hearing.purposeOrProceeding,
            notes                = hearing.notes,
            hearingDate          = hearing.hearingDate,
            isVisible            = true
        )
    }

    // ── CRUD ──────────────────────────────────────────────────
    fun saveHearing() {
        val form = _formState.value
        if (!form.isValid) return
        viewModelScope.launch {
            runCatching {
                val hearing = Hearing(
                    id                  = form.editingId ?: 0L,
                    caseId              = caseId,
                    hearingDate         = form.hearingDate,
                    purposeOrProceeding = form.purposeOrProceeding.trim(),
                    notes               = form.notes.trim()
                )
                if (form.editingId != null) repo.updateHearing(hearing)
                else repo.insertHearing(hearing)
            }.onSuccess {
                _formState.value = HearingFormState(isVisible = true) // clear, stay open
            }.onFailure {
                _events.send(UiEvent.ShowToast("Failed to save hearing"))
            }
        }
    }

    fun deleteHearing(hearingId: Long) {
        viewModelScope.launch {
            runCatching { repo.deleteHearing(hearingId) }
                .onFailure { _events.send(UiEvent.ShowToast("Failed to delete hearing")) }
        }
    }
}