package com.example.laposada.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.laposada.dataClass.FoodTypeDataClass

@Dao
interface DaoFoodType {
    @Query("SELECT * FROM foodtypedataclass")
    suspend fun getAll(): List<FoodTypeDataClass>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertAll(foodtypeList: List<FoodTypeDataClass>)

    @Delete
    suspend fun delete(foodDataClass: FoodTypeDataClass)

    @Query("DELETE FROM FoodTypeDataClass WHERE id = :idFood")
    suspend fun deleteById(idFood: Int)

    @Query("DELETE FROM FoodTypeDataClass")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(foodtype: FoodTypeDataClass)

    @Query("SELECT nombre FROM foodtypedataclass")
    suspend fun getNombres(): List<String>

    @Query("SELECT nombre FROM FoodTypeDataClass WHERE id = :idFood")
    suspend fun selectById(idFood: Int): String
}