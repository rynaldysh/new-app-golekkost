package com.mavendra.golekkostv3.helper

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Helper {

    fun gantiRupiah(string: String): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(Integer.valueOf(string))
    }

    fun gantiRupiah(value: Int): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(value)
    }

    fun gantiRupiah(value: Boolean): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(value)
    }

    fun setToolbar(activity: Activity, toolbarr: Toolbar, title: String) {
        (activity as AppCompatActivity).setSupportActionBar(toolbarr)
        activity.supportActionBar!!.title = title
        activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun setToolbarSecond(activity: Activity, toolbarr: Toolbar, title: String) {
        (activity as AppCompatActivity).setSupportActionBar(toolbarr)
        activity.supportActionBar!!.title = title
        activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    fun convertDate(date: String, newFormat: String, oldFormat: String = "yyyy-MM-dd kk:mm:ss") :String{
        val dateFormat = SimpleDateFormat(oldFormat)
        val convert = dateFormat.parse(date)
        dateFormat.applyPattern(newFormat)
        return dateFormat.format(convert)
    }
}