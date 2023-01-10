package com.mavendra.golekkostv3.room

import android.app.Activity
import android.content.Context
import androidx.room.*
import com.mavendra.golekkostv3.model.*


@TypeConverters
@Database(entities = [Barang::class, Alamat::class, Jasaangkut::class, AlamatPesanJasa::class,
                    AlamatPesanKostKontrakan::class, Kostkontrakan::class] /* List model Ex:NoteModel */, version = 1)
abstract class MyDatabase : RoomDatabase() {

    abstract fun daoKeranjang(): DaoKeranjang // DaoNote
    abstract fun daoAlamat(): DaoAlamat // DaoNote
    abstract fun daoSimpanJasa(): DaoSimpanJasa // DaoNote
    abstract fun daoSimpanKostKontrakan(): DaoSimpanKostKontrakan // DaoNote
    abstract fun daoAlamatPesanJasa(): DaoAlamatPesanJasa // DaoNote
    abstract fun daoAlamatPesanKostKontrakan(): DaoAlamatPesanKostKontrakan // DaoNote


    companion object {
        private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase? {
            if (INSTANCE == null) {
                synchronized(MyDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java, "coba37" // Database Name
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}