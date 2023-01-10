package com.mavendra.golekkostv3.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "simpanjasa") // the name of tabel
public class Jasaangkut implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    public int idTb;

    public int id;
    public String name;
    public String nomortelfon;
    public String harga;
    public String lokasi;
    public String deskripsi;
    public String image;
    public String created_at;
    public String updated_at;

    public  int jumlah = 1;
    public  boolean selected = true;

}
