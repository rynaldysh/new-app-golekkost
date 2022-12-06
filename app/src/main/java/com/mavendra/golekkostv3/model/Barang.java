package com.mavendra.golekkostv3.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "keranjang") // the name of tabel
public class Barang implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    public int idTb;

    public int id;
    public String user_id;
    public String name_pemilik;
    public String notelfon;
    public String name;
    public String harga;
    public String lokasi;
    public String deskripsi;
    public String image;
    public String status;
    public String kode_input_barang;
    public String created_att;

    public  int jumlah = 1;
    public  boolean selected = true;

}
