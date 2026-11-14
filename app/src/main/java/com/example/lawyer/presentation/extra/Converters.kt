package com.example.lawyer.presentation.extra

import androidx.room.TypeConverter
import androidx.room3.ColumnTypeConverter
import androidx.room3.ColumnTypeConverters
import org.json.JSONArray

class Converters {

    @ColumnTypeConverter
    fun fromStringList(value: List<String>): String {
        val jsonArray = JSONArray()
        value.forEach { jsonArray.put(it) }
        return jsonArray.toString()
    }

    @ColumnTypeConverter
    fun toStringList(value: String): List<String> {
        return runCatching {
            val jsonArray = JSONArray(value)
            List(jsonArray.length()) { index ->
                jsonArray.getString(index)
            }
        }.getOrDefault(emptyList())
    }
}