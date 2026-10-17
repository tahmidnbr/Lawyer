package com.example.lawyer.logic.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lawyer.logic.repo.CaseRepository
import com.example.lawyer.presentation.model.Hearing
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar



data class DateUiState(
    val allHearings: List<Hearing> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class DateVm(private val repo: CaseRepository) : ViewModel() {

    val uiState: StateFlow<DateUiState> =
        repo.getAllHearings()
            .map { DateUiState(allHearings = it, isLoading = false) }
            .catch { e -> emit(DateUiState(isLoading = false, errorMessage = e.message)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DateUiState(isLoading = true)
            )

    /** Days that have hearings in the given year+month */
    fun hearingDaysFor(hearings: List<Hearing>, year: Int, month: Int): Set<Int> {
        val cal = Calendar.getInstance()
        return hearings.filter { h ->
            cal.timeInMillis = h.hearingDate
            cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month
        }.map { h ->
            cal.timeInMillis = h.hearingDate
            cal.get(Calendar.DAY_OF_MONTH)
        }.toSet()
    }

    /** Hearings on a specific day */
    fun hearingsForDay(hearings: List<Hearing>, year: Int, month: Int, day: Int): List<Hearing> {
        val cal = Calendar.getInstance()
        return hearings.filter { h ->
            cal.timeInMillis = h.hearingDate
            cal.get(Calendar.YEAR)         == year  &&
                    cal.get(Calendar.MONTH)        == month &&
                    cal.get(Calendar.DAY_OF_MONTH) == day
        }
    }
}