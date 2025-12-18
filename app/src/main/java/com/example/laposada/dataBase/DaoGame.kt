package com.example.laposada.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.laposada.dataClass.FoodDataClass
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

    @Query("SELECT * FROM games WHERE id = :idGame")
    suspend fun getById(idGame: Int): GameDataClass


    @Query("DELETE FROM games")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(food: GameDataClass)

    @Update
    suspend fun update(food: GameDataClass)
}