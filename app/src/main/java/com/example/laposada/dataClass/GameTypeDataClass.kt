package com.example.laposada.dataClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameTypeDataClass(
    @PrimaryKey(autoGenerate = true)    val id: Int,
    @ColumnInfo(name = "nombre")        val nombre: String
)
