package com.example.lawyer.presentation.model

import androidx.room3.Embedded
import androidx.room3.Relation

data class CaseWithHearings(
    @Embedded val case: LegalCase,
    @Relation(
        parentColumns = ["id"],
        entityColumns = ["caseId"]
    )
    val hearings: List<Hearing> = emptyList()
) {
    /** Next upcoming hearing — soonest future date */
    val nextHearing: Hearing?
        get() = hearings
            .filter { it.hearingDate > System.currentTimeMillis() }
            .minByOrNull { it.hearingDate }

    /** Hearings sorted newest → oldest for timeline display */
    val hearingsSorted: List<Hearing>
        get() = hearings.sortedByDescending { it.hearingDate }
}
