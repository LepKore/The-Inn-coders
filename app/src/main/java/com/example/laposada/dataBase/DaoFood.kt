package com.example.laposada.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.laposada.dataClass.FoodDataClass

@Dao
interface DaoFood {
    @Query("SELECT * FROM fooddataclass")
    fun getAll(): List<FoodDataClass>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertAll(foodList: List<FoodDataClass>)

    @Delete
    fun delete(foodDataClass: FoodDataClass)

    @Query("DELETE FROM fooddataclass WHERE id = :idFood")
    suspend fun deleteById(idFood: Int)

    @Query("DELETE FROM fooddataclass")
    suspend fun deleteAll()
}