package com.example.laposada.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.laposada.converters.Converters
import com.example.laposada.dataClass.FoodDataClass
import com.example.laposada.dataClass.FoodTypeDataClass
import com.example.laposada.dataClass.GameDataClass

@Database(entities = arrayOf(
    FoodDataClass::class,
    GameDataClass::class,
    FoodTypeDataClass::class), version = 2)
@TypeConverters(Converters::class)
abstract class GeneralDataBase: RoomDatabase() {

    abstract fun DaoFood() : DaoFood
    abstract fun DaoFoodType(): DaoFoodType
    abstract fun DaoGame(): DaoGame
}