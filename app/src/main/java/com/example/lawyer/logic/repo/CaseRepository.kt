package com.example.lawyer.logic.repo

import com.example.lawyer.logic.room.HearingDao
import com.example.lawyer.logic.room.LegalCaseDao
import com.example.lawyer.presentation.model.CaseWithHearings
import com.example.lawyer.presentation.model.Hearing
import com.example.lawyer.presentation.model.LegalCase
import kotlinx.coroutines.flow.Flow

class CaseRepository(
    private val caseDao: LegalCaseDao,
    private val hearingDao: HearingDao
) {
    // ── LegalCase ─────────────────────────────────────────────

    suspend fun insertCase(case: LegalCase): Long =
        caseDao.insertCase(case)

    suspend fun updateCase(case: LegalCase) =
        caseDao.updateCase(case)

    suspend fun deleteCase(caseId: Long) =
        caseDao.deleteCaseById(caseId)

    suspend fun getCaseById(caseId: Long): LegalCase? =
        caseDao.getCaseById(caseId)

    fun getAllCases(): Flow<List<LegalCase>> =
        caseDao.getAllCases()

    fun getPriorityCases(): Flow<List<LegalCase>> =
        caseDao.getPriorityCases()

    fun getAllCasesWithHearings(): Flow<List<CaseWithHearings>> =
        caseDao.getAllCasesWithHearings()

    fun getPriorityCasesWithHearings(): Flow<List<CaseWithHearings>> =
        caseDao.getPriorityCasesWithHearings()

    fun getCaseWithHearings(caseId: Long): Flow<CaseWithHearings?> =
        caseDao.getCaseWithHearings(caseId)

    // ── Hearing ───────────────────────────────────────────────

    suspend fun insertHearing(hearing: Hearing): Long =
        hearingDao.insertHearing(hearing)

    suspend fun updateHearing(hearing: Hearing) =
        hearingDao.updateHearing(hearing)

    suspend fun deleteHearing(hearingId: Long) =
        hearingDao.deleteHearingById(hearingId)

    fun getHearingsByCaseId(caseId: Long): Flow<List<Hearing>> =
        hearingDao.getHearingsByCaseId(caseId)

    fun getAllHearings(): Flow<List<Hearing>> =
        hearingDao.getAllHearings()

    fun getHearingsBetween(from: Long, to: Long): Flow<List<Hearing>> =
        hearingDao.getHearingsBetween(from, to)
}