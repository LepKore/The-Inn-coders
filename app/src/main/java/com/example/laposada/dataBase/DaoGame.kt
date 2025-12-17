package com.example.laposada.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.laposada.dataClass.GameDataClass

@Dao
interface DaoGame {
    @Query("SELECT * FROM games")
    suspend fun getAll(): List<GameDataClass>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertAll(foodList: List<GameDataClass>)

    @Delete
    fun delete(foodDataClass: GameDataClass)

    @Query("DELETE FROM games WHERE id = :idGame")
    suspend fun deleteById(idGame: Int)

    @Query("DELETE FROM games")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(food: GameDataClass)
}