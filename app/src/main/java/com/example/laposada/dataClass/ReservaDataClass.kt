package com.example.laposada.dataClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReservaDataClass(
    @PrimaryKey(autoGenerate = true)    val id: Int,
    @ColumnInfo(name = "mesa")          val mesa: Int,
    @ColumnInfo(name = "horaInicio")    val horaInicio: String,
    @ColumnInfo(name = "horaFin")       val horaFin: String,
    @ColumnInfo(name = "dia")           val dia: String,
    @ColumnInfo(name = "juego")         val juego: Int?
)
