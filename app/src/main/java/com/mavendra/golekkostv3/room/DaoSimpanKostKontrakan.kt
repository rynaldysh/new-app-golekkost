package com.mavendra.golekkostv3.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.mavendra.golekkostv3.model.Kostkontrakan

@Dao
interface DaoSimpanKostKontrakan {

    @Insert(onConflict = REPLACE)
    fun insert(data: Kostkontrakan)

    @Delete
    fun delete(data: Kostkontrakan)

    @Delete
    fun delete(data: List<Kostkontrakan>)

    @Update
    fun update(data: Kostkontrakan): Int

    @Query("SELECT * from simpankostkontrakan ORDER BY id ASC")
    fun getAll(): List<Kostkontrakan>

    @Query("SELECT * FROM simpankostkontrakan WHERE id = :id LIMIT 1")
    fun getKostkostkontrakan(id: Int): Kostkontrakan

    @Query("DELETE FROM simpankostkontrakan WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("DELETE FROM simpankostkontrakan")
    fun deleteAll(): Int
}