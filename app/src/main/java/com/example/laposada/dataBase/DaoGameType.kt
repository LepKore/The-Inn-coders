package com.example.laposada.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.laposada.dataClass.GameTypeDataClass

@Dao
interface DaoGameType {
    @Query("SELECT * FROM gametypedataclass")
    suspend fun getAll(): List<GameTypeDataClass>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertAll(gameTypeDataClassList: List<GameTypeDataClass>)

    @Delete
    suspend fun delete(gameTypeDataClass: GameTypeDataClass)

    @Query("DELETE FROM gametypedataclass WHERE id = :idGame")
    suspend fun deleteById(idGame: Int)

    @Query("DELETE FROM GameTypeDataClass")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(gameTypeDataClass: GameTypeDataClass)

    @Query("SELECT nombre FROM gametypedataclass")
    suspend fun getNombres(): List<String>

    @Query("SELECT nombre FROM GameTypeDataClass WHERE id = :idGame")
    suspend fun selectById(idGame: Int): String
}