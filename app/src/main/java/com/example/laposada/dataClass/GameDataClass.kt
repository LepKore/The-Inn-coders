package com.example.laposada.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.laposada.converters.Converters

@Entity(tableName = "games")
@TypeConverters(Converters::class)
data class GameDataClass(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val imagen1: Int,
    val imagen2: Int,
    val descripcion: String,
    val categorias: List<String>
)