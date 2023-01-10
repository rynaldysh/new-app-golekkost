package com.mavendra.golekkostv3.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.mavendra.golekkostv3.model.AlamatPesanJasa
import com.mavendra.golekkostv3.model.AlamatPesanKostKontrakan

@Dao
interface DaoAlamatPesanJasa {

    @Insert(onConflict = REPLACE)
    fun insert(data: AlamatPesanJasa)

    @Delete
    fun delete(data: AlamatPesanJasa)

    @Update
    fun update(data: AlamatPesanJasa): Int

    @Query("SELECT * from alamatpesanjasa ORDER BY id ASC")
    fun getAll(): List<AlamatPesanJasa>

    @Query("SELECT * FROM alamatpesanjasa WHERE id = :id LIMIT 1")
    fun getBarang(id: Int): AlamatPesanJasa

    @Query("SELECT * FROM alamatpesanjasa WHERE isSelected = :status LIMIT 1")
    fun getByStatus(status: Boolean): AlamatPesanJasa?

    @Query("DELETE FROM alamatpesanjasa")
    fun deleteAll(): Int
}