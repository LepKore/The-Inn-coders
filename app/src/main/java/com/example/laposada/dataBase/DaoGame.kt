package com.example.laposada.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.laposada.dataClass.GameDataClass

@Dao
interface DaoGame {
    @Query("SELECT * FROM games")
    suspend fun getAll(): List<GameDataClass>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<GameDataClass>)
}