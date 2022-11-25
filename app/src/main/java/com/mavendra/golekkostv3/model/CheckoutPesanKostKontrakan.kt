package com.mavendra.golekkostv3.model

class CheckoutPesanKostKontrakan {
    lateinit var usergeneral_id: String
    lateinit var name: String
    lateinit var tanggal: String
    lateinit var phone: String
    lateinit var detail_lokasi: String
    var kostkontrakans = ArrayList<Item>()

    class Item{
        lateinit var id: String

    }
}