package com.mavendra.golekkostv3.adapter

import android.app.Activity
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
import com.mavendra.golekkostv3.activity.DetailJasaAngkutActivity
import com.mavendra.golekkostv3.app.Constants
import com.mavendra.golekkostv3.model.Jasaangkut
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class JasaAngkutAdapter(var activity: Activity, var data: ArrayList<Jasaangkut>):RecyclerView.Adapter<JasaAngkutAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tvNameJasa)
        val tvHarga = view.findViewById<TextView>(R.id.tvHargaJasa)
        val tvLokasi = view.findViewById<TextView>(R.id.tvLokasiJasa)
        val ivJasa = view.findViewById<ImageView>(R.id.ivJasa)
        val layout = view.findViewById<CardView>(R.id.layoutJasa)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_jasaangkut, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvName.text = data[position].name
        holder.tvHarga.text = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(Integer.valueOf(data[position].harga))
        holder.tvLokasi.text = data[position].lokasi
        val image =  Constants.JASAANGKUT_URL + data[position].image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.beranda_ex_kostt)
            .error(R.drawable.beranda_ex_kostt)
            .into(holder.ivJasa)

        holder.layout.setOnClickListener {
            val jasaAngkut = Intent(activity, DetailJasaAngkutActivity::class.java)
            val str = Gson().toJson(data[position], Jasaangkut::class.java)
            jasaAngkut.putExtra("jasaangkut", str)
            activity.startActivity(jasaAngkut)
        }
    }
}