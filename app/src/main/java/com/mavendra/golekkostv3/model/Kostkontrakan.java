package com.mavendra.golekkostv3.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "simpankostkontrakan") // the name of tabel
public class Kostkontrakan implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    public int idTb;

    public int id;
    public String name;
    public String nomortelfon;
    public String pengelola;
    public String harga;
    public String rasiobayar;
    public String lokasi;
    public String sisakamar;
    public String totalkamar;
    public String mayoritas;
    public String deskripsi;
    public String listrik;
    public String air;
    public String wifi;
    public String bed;
    public String ac;
    public String kamarmandi;
    public String kloset;
    public String satpam;
    public String image;
    public String created_at;
    public String updated_at;

    public  int jumlah = 1;
    public  boolean selected = true;
}
