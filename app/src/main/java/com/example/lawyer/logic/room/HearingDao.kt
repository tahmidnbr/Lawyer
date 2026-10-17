package com.example.lawyer.logic.room

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.example.lawyer.presentation.model.Hearing
import kotlinx.coroutines.flow.Flow

@Dao
interface HearingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHearing(hearing: Hearing): Long

    @Update
    suspend fun updateHearing(hearing: Hearing)

    @Query("DELETE FROM hearings WHERE id = :hearingId")
    suspend fun deleteHearingById(hearingId: Long)

    // Write case screen — hearings for one case
    @Query("SELECT * FROM hearings WHERE caseId = :caseId ORDER BY hearingDate DESC")
    fun getHearingsByCaseId(caseId: Long): Flow<List<Hearing>>

    // Date screen — all hearings across all cases
    @Query("SELECT * FROM hearings ORDER BY hearingDate ASC")
    fun getAllHearings(): Flow<List<Hearing>>

    // Date screen — hearings within a date range (for month filtering)
    @Query("""
        SELECT * FROM hearings 
        WHERE hearingDate >= :from AND hearingDate <= :to 
        ORDER BY hearingDate ASC
    """)
    fun getHearingsBetween(from: Long, to: Long): Flow<List<Hearing>>
}