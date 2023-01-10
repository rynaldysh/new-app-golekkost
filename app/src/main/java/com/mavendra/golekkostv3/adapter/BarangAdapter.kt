package com.mavendra.golekkostv3.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.activity.DetailBarangActivity
import com.mavendra.golekkostv3.app.Constants
import com.mavendra.golekkostv3.model.Barang
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class BarangAdapter(var activity: Activity, var data:ArrayList<Barang>):RecyclerView.Adapter<BarangAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tvNamaBarang)
        val tvHarga = view.findViewById<TextView>(R.id.tvHargaBarang)
        val tvLokasi = view.findViewById<TextView>(R.id.tvLokasiBarang)
        val ivBarang = view.findViewById<ImageView>(R.id.ivBarang)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatusBarang)
        val layout = view.findViewById<CardView>(R.id.layoutBarang)


    }

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_barang, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvName.text = data[position].name
        holder.tvHarga.text = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(Integer.valueOf(data[position].harga))
        holder.tvLokasi.text = data[position].lokasi
        holder.tvStatus.text = data[position].status

        var color = context.getColor(R.color.menunggu)

        when (data[position].status) {
            "TERSEDIA" -> color = context.getColor(R.color.tersedia)
            "TERJUAL" -> color = context.getColor(R.color.terjual)
        }

        holder.tvStatus.setBackgroundColor(color)

        val image =  Constants.barang_url + data[position].image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.beranda_ex_kostt)
            .error(R.drawable.beranda_ex_kostt)
            .into(holder.ivBarang)

        holder.layout.setOnClickListener {
            val barang = Intent(activity, DetailBarangActivity::class.java)
            val str = Gson().toJson(data[position], Barang::class.java)
            barang.putExtra("barang", str)
            activity.startActivity(barang)
        }
    }
}