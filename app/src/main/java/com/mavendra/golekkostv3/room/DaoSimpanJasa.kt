package com.mavendra.golekkostv3.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.mavendra.golekkostv3.model.Jasaangkut

@Dao
interface DaoSimpanJasa {

    @Insert(onConflict = REPLACE)
    fun insert(data: Jasaangkut)

    @Delete
    fun delete(data: Jasaangkut)

    @Delete
    fun delete(data: List<Jasaangkut>)

    @Update
    fun update(data: Jasaangkut): Int

    @Query("SELECT * from simpanjasa ORDER BY id ASC")
    fun getAll(): List<Jasaangkut>

    @Query("SELECT * FROM simpanjasa WHERE id = :id LIMIT 1")
    fun getJasaangkut(id: Int): Jasaangkut

    @Query("DELETE FROM simpanjasa WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("DELETE FROM simpanjasa")
    fun deleteAll(): Int
}