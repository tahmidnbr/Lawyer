package com.example.lawyer.logic.room

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Update
import com.example.lawyer.presentation.model.CaseWithHearings
import com.example.lawyer.presentation.model.LegalCase
import kotlinx.coroutines.flow.Flow

@Dao
interface LegalCaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCase(case: LegalCase): Long

    @Update
    suspend fun updateCase(case: LegalCase)

    @Query("DELETE FROM legal_cases WHERE id = :caseId")
    suspend fun deleteCaseById(caseId: Long)

    // Case screen — all cases
    @Query("SELECT * FROM legal_cases ORDER BY creatingDate DESC")
    fun getAllCases(): Flow<List<LegalCase>>

    // Home screen — priority only
    @Query("SELECT * FROM legal_cases WHERE isPriority = 1 ORDER BY creatingDate DESC")
    fun getPriorityCases(): Flow<List<LegalCase>>

    @Query("SELECT * FROM legal_cases WHERE id = :caseId LIMIT 1")
    suspend fun getCaseById(caseId: Long): LegalCase?

    // Case details — case + all its hearings
    @Transaction
    @Query("SELECT * FROM legal_cases WHERE id = :caseId LIMIT 1")
    fun getCaseWithHearings(caseId: Long): Flow<CaseWithHearings?>

    // Case screen overview — all cases with their hearings
    @Transaction
    @Query("SELECT * FROM legal_cases ORDER BY creatingDate DESC")
    fun getAllCasesWithHearings(): Flow<List<CaseWithHearings>>

    // Priority cases with hearings for home
    @Transaction
    @Query("SELECT * FROM legal_cases WHERE isPriority = 1 ORDER BY creatingDate DESC")
    fun getPriorityCasesWithHearings(): Flow<List<CaseWithHearings>>
}