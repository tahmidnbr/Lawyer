package com.example.lawyer.logic.room

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.lawyer.presentation.model.Hearing
import com.example.lawyer.presentation.model.LegalCase

@Database(
    entities = [LegalCase::class, Hearing::class],
    version = 1,
    exportSchema = false
)
abstract class CaseDatabase : RoomDatabase(){
    abstract fun legalCaseDao(): LegalCaseDao
    abstract fun hearingDao(): HearingDao
}