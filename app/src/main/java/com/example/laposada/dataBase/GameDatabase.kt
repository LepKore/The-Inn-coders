package com.example.laposada.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.laposada.converters.Converters
import com.example.laposada.dataClass.GameDataClass

@Database(entities = [GameDataClass::class], version = 2)
@TypeConverters(Converters::class)
abstract class GameDatabase : RoomDatabase() {
    abstract fun DaoGame(): DaoGame
}