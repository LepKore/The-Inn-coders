package com.example.laposada.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.laposada.dataClass.GameDataClass

@Database(entities = [GameDataClass::class], version = 1)
abstract class GameDatabase : RoomDatabase() {
    abstract fun DaoGame(): DaoGame
}