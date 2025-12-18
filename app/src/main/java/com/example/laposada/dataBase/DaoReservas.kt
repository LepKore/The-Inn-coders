package com.example.laposada.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.laposada.dataClass.ReservaDataClass

@Dao
interface DaoReservas {
    @Query("SELECT * FROM reservadataclass")
    suspend fun getAll(): List<ReservaDataClass>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertAll(resList: List<ReservaDataClass>)

    @Delete
    fun delete(reservaDataClass: ReservaDataClass)

    @Query("DELETE FROM ReservaDataClass WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM ReservaDataClass")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(res: ReservaDataClass)

    @Update
    suspend fun update(res: ReservaDataClass)


}