package com.example.laposada.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameDataClass(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val imagen: Int
)