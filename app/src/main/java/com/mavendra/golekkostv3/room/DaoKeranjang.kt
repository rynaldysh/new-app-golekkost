package com.mavendra.golekkostv3.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.mavendra.golekkostv3.model.Barang

@Dao
interface DaoKeranjang {

    @Insert(onConflict = REPLACE)
    fun insert(data: Barang)

    @Delete
    fun delete(data: Barang)

    @Delete
    fun delete(data: List<Barang>)

    @Update
    fun update(data: Barang): Int

    @Query("SELECT * from keranjang ORDER BY id ASC")
    fun getAll(): List<Barang>

    @Query("SELECT * FROM keranjang WHERE id = :id LIMIT 1")
    fun getBarang(id: Int): Barang

    @Query("DELETE FROM keranjang WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("DELETE FROM keranjang")
    fun deleteAll(): Int
}