package com.example.laposada.converters

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromListInt(value: List<Int>): String =
        value.joinToString(",")

    @TypeConverter
    fun toListInt(value: String): List<Int> =
        if (value.isEmpty()) emptyList()
        else value.split(",").map { it.toInt() }
}