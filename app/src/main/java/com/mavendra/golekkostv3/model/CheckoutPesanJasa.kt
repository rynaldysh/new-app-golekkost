package com.mavendra.golekkostv3.model

class CheckoutPesanJasa {
    lateinit var usergeneral_id: String
    lateinit var name: String
    lateinit var tanggal: String
    lateinit var phone: String
    lateinit var detail_lokasi_asal: String
    lateinit var detail_lokasi_tujuan: String
    lateinit var type_asal: String
    lateinit var type_tujuan: String
    var jasaangkuts = ArrayList<Item>()

    class Item{
        lateinit var id: String

    }
}