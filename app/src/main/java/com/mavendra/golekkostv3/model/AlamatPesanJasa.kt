package com.mavendra.golekkostv3.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alamatpesanjasa") // the name of tabel
// the name of tabel
class AlamatPesanJasa {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    var idTb = 0

    var id = 0
    var name = ""
    var phone = ""
    var detail_lokasi_asal = ""
    var detail_lokasi_tujuan = ""
    var type_asal = ""
    var type_tujuan = ""
    var tanggal = ""

    var isSelected = true


}