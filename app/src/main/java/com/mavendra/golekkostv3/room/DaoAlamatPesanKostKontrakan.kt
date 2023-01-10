package com.mavendra.golekkostv3.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.mavendra.golekkostv3.model.AlamatPesanKostKontrakan

@Dao
interface DaoAlamatPesanKostKontrakan {

    @Insert(onConflict = REPLACE)
    fun insert(data: AlamatPesanKostKontrakan)

    @Delete
    fun delete(data: AlamatPesanKostKontrakan)

    @Update
    fun update(data: AlamatPesanKostKontrakan): Int

    @Query("SELECT * from alamatpesankostkontrakan ORDER BY id ASC")
    fun getAll(): List<AlamatPesanKostKontrakan>

    @Query("SELECT * FROM alamatpesankostkontrakan WHERE id = :id LIMIT 1")
    fun getBarang(id: Int): AlamatPesanKostKontrakan

    @Query("SELECT * FROM alamatpesankostkontrakan WHERE isSelected = :status LIMIT 1")
    fun getByStatus(status: Boolean): AlamatPesanKostKontrakan?

    @Query("DELETE FROM alamatpesankostkontrakan")
    fun deleteAll(): Int
}