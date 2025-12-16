package com.example.laposada.dataClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FoodDataClass(
    @PrimaryKey(autoGenerate = true)    val id: Int,
    @ColumnInfo(name = "nombre")        val nombre: String,
    @ColumnInfo(name = "precio")        val precio: Double,
    @ColumnInfo(name = "imagen")        val imagen: Int,
    @ColumnInfo(name = "descripcion")   val descripcion: String,
    @ColumnInfo(name = "tipos")         val tipos: List<String>
)
