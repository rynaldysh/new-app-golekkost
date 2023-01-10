package com.mavendra.golekkostv3.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.mavendra.golekkostv3.model.Usergeneral

class SharedPref (val activity: Activity){

    val login = "login"
    val nama = "nama"
    val phone = "phone"
    val email = "email"
    val id = "id"

    val user = "user"

    val mypref = "MAIN_PRF"
    val sp:SharedPreferences

    init {
        sp = activity.getSharedPreferences(mypref, Context.MODE_PRIVATE)
    }

    fun setStatusLogin(status:Boolean){
        sp.edit().putBoolean(login, status).apply()
    }

    fun getStatusLogin():Boolean{
        return sp.getBoolean(login, false)
    }

    fun setUser(value: Usergeneral){//merubah dari class ke string
        val data: String = Gson().toJson(value, Usergeneral::class.java)
        sp.edit().putString(user, data).apply()
    }

    fun getUser(): Usergeneral?{//merubah dari string ke class(objek Usergeneral)
        val data:String = sp.getString(user, null)?:return null
        return Gson().fromJson<Usergeneral>(data, Usergeneral::class.java)
    }

    fun setString(key: String, vlaue: String){
        sp.edit().putString(key, vlaue).apply()
    }

    fun getString(key: String): String{
        return sp.getString(key, "")!!
    }

}