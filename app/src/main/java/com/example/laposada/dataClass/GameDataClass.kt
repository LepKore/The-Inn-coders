package com.example.laposada.dataClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameDataClass(
    @PrimaryKey(autoGenerate = true)    val id: Int,
    @ColumnInfo(name = "nombre")        val nombre: String,
    @ColumnInfo(name = "imagenCaja")    val imagenCaja: String,
    @ColumnInfo(name = "imagenGame")    val imagenGame: String,
    @ColumnInfo(name = "descripcion")   val descripcion: String,
    @ColumnInfo(name = "categorias")    val categorias: List<Int>

)