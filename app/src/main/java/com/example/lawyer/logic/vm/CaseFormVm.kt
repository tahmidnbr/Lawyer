package com.example.lawyer.logic.vm

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lawyer.logic.repo.CaseRepository
import com.example.lawyer.presentation.UiEvent
import com.example.lawyer.presentation.model.LegalCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CaseFormUiState(
    val caseId: Long? = null,
    val caseNo: String = "",
    val plaintiff: String = "",
    val defendant: String = "",
    val representing: String = "",
    val courtName: String = "",
    val caseFee: String = "",       // Kept as String for text input flexibility
    val caseType: String = "",
    val isPriority: Boolean = false,
    val selectedImageUri: String? = null, // Stored as String? for Room compatibility
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isEditMode: Boolean get() = caseId != null
    val title: String get() = "$plaintiff vs $defendant"
    val isFormValid: Boolean
        get() = caseNo.isNotBlank() &&
                plaintiff.isNotBlank() &&
                defendant.isNotBlank() &&
                representing.isNotBlank()
}

class CaseFormVm(
    private val repo: CaseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val editCaseId: Long? =
        savedStateHandle.get<String>("caseId")?.toLongOrNull()?.takeIf { it != -1L }

    private val _uiState = MutableStateFlow(CaseFormUiState())
    val uiState: StateFlow<CaseFormUiState> = _uiState.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events: Flow<UiEvent> = _events.receiveAsFlow()

    init { editCaseId?.let { loadCase(it) } }

    private fun loadCase(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching { repo.getCaseById(id) }
                .onSuccess { case ->
                    if (case != null) {
                        _uiState.update {
                            it.copy(
                                caseId        = case.id,
                                caseNo        = case.caseNo,
                                plaintiff     = case.plaintiff,
                                defendant     = case.defendant,
                                representing  = case.representing,
                                courtName     = case.courtName,
                                caseFee       = case.caseFee?.toString() ?: "",
                                caseType      = case.caseType,
                                isPriority    = case.isPriority,
                                selectedImageUri = case.imageUri, // Load saved image URI if present
                                isLoading     = false
                            )
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Case not found") }
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
        }
    }

    // ── Field updaters ────────────────────────────────────────
    fun onCaseNoChange(v: String)       = _uiState.update { it.copy(caseNo = v,       errorMessage = null) }
    fun onPlaintiffChange(v: String)    = _uiState.update { it.copy(plaintiff = v,    errorMessage = null) }
    fun onDefendantChange(v: String)    = _uiState.update { it.copy(defendant = v,    errorMessage = null) }
    fun onRepresentingChange(v: String) = _uiState.update { it.copy(representing = v, errorMessage = null) }
    fun onCourtNameChange(v: String)    = _uiState.update { it.copy(courtName = v) }
    fun onCaseFeeChange(v: String)      = _uiState.update { it.copy(caseFee = v) }
    fun onCaseTypeChange(v: String)     = _uiState.update { it.copy(caseType = v) }
    fun onPriorityToggle()              = _uiState.update { it.copy(isPriority = !it.isPriority) }

    // Image selection updater
    fun onImageSelected(uri: Uri?)      = _uiState.update { it.copy(selectedImageUri = uri?.toString()) }

    // ── Save ──────────────────────────────────────────────────
    fun saveCase() {
        val state = _uiState.value
        if (!state.isFormValid) {
            _uiState.update { it.copy(errorMessage = "Case No, plaintiff, defendant and representing are required") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching {
                val case = LegalCase(
                    id = state.caseId ?: 0L,
                    caseNo = state.caseNo.trim(),
                    plaintiff = state.plaintiff.trim(),
                    defendant = state.defendant.trim(),
                    representing = state.representing.trim(),
                    courtName = state.courtName.trim(),
                    caseFee = state.caseFee.toDoubleOrNull(),
                    caseType = state.caseType.trim(),
                    isPriority = state.isPriority,
                    imageUri = state.selectedImageUri // Save the image URI/path to entity
                )
                if (state.isEditMode) { repo.updateCase(case); null }
                else repo.insertCase(case)
            }.onSuccess { newId ->
                _uiState.update { it.copy(isLoading = false) }
                _events.send(
                    if (state.isEditMode) UiEvent.NavigateTo("details/${state.caseId}")
                    else UiEvent.NavigateTo("write/$newId")
                )
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}