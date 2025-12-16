package com.example.laposada.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.laposada.converters.Converters
import com.example.laposada.dataClass.FoodDataClass

@Database(entities = arrayOf(FoodDataClass::class), version = 1)
@TypeConverters(Converters::class)
abstract class FoodDatabase: RoomDatabase() {
    abstract fun DaoFood() : DaoFood
}