package com.example.lawyer.presentation.model

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey
@Entity(
    tableName = "hearings",
    foreignKeys = [
        ForeignKey(
            entity = LegalCase::class,
            parentColumns = ["id"],
            childColumns = ["caseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["caseId"])]
)
data class Hearing(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val caseId: Long,
    val hearingDate: Long,
    val purposeOrProceeding: String,         // what happened / what is scheduled
    val notes: String = "",
    val addedAt: Long = System.currentTimeMillis()
)
