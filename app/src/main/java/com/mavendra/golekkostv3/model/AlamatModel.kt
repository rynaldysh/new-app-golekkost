package com.mavendra.golekkostv3.model

class AlamatModel {
    val id = 0
    val nama = ""

    val status = Status()
    val results = ArrayList<Provinsi>()

    class Status{
        val code = 0
        val description = ""
    }

    class Provinsi{
        val province_id = ""
        val province = ""
        val city_id = ""
        val city_name = ""
        val postal_code = ""
        val type = ""
    }
}