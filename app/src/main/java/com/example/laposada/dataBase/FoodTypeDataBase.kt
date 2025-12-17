package com.example.laposada.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.laposada.dataClass.FoodTypeDataClass
import com.example.laposada.converters.Converters
import com.example.laposada.dataClass.FoodDataClass

@Database(entities = arrayOf(FoodTypeDataClass::class), version = 1)
abstract class FoodTypeDataBase: RoomDatabase() {
     abstract fun DaoFoodType() : DaoFoodType
}