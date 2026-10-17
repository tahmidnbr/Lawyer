package com.example.lawyer.presentation.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "legal_cases")
data class LegalCase(
    @PrimaryKey(autoGenerate = true)  val id: Long = 0,

    val caseNo: String,
    val plaintiff: String,
    val defendant: String,
    val representing: String,

    val courtName: String  = "",
    val caseType: String  = "",
    val caseFee: Double? =  null,

    val isPriority: Boolean = false,         // home screen filter
    val creatingDate: Long = System.currentTimeMillis()
){
    val title: String get() = "$plaintiff vs $defendant"
}
