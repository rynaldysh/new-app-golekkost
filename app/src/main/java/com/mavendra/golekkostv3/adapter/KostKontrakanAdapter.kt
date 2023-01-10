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
import com.mavendra.golekkostv3.activity.DetailKostKontrakanActivity
import com.mavendra.golekkostv3.app.Constants.KOSTKONTRAKAN_URL
import com.mavendra.golekkostv3.model.Kostkontrakan
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class KostKontrakanAdapter(var activity: Activity, var data:ArrayList<Kostkontrakan>):RecyclerView.Adapter<KostKontrakanAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tvNameKost)
        val tvHarga = view.findViewById<TextView>(R.id.tvHargaKost)
        val tvRasio = view.findViewById<TextView>(R.id.tvRasioPembayaranKost)
        val tvLokasi = view.findViewById<TextView>(R.id.tvLokasiKost)
        val tvMayoritas = view.findViewById<TextView>(R.id.tvMayoritas)
        val tvDeskripsi = view.findViewById<TextView>(R.id.tvDeskripsiKost)
        val ivKost = view.findViewById<ImageView>(R.id.ivKost)
        val layout = view.findViewById<CardView>(R.id.layoutKos)

    }

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_kostkontrakan, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvName.text = data[position].name
        holder.tvHarga.text = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(Integer.valueOf(data[position].harga))
        holder.tvRasio.text = data[position].rasiobayar
        holder.tvLokasi.text = data[position].lokasi
        holder.tvMayoritas.text = data[position].mayoritas
        holder.tvDeskripsi.text = data[position].deskripsi
        //holder.ivKost.setImageResource(data[position].image)

        var color = context.getColor(R.color.mahasiswa)
        when (data[position].mayoritas) {
            "Mahasiswa" -> color = context.getColor(R.color.mahasiswa)
            "Pegawai" -> color = context.getColor(R.color.pegawai)
            "Campuran" -> color = context.getColor(R.color.campuran)
        }

        holder.tvMayoritas.setBackgroundColor(color)

        val image =  KOSTKONTRAKAN_URL + data[position].image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.beranda_ex_kostt)
            .error(R.drawable.beranda_ex_kostt)
            .into(holder.ivKost)

        holder.layout.setOnClickListener {
            val kostKontrakan = Intent(activity, DetailKostKontrakanActivity::class.java)
            val str = Gson().toJson(data[position], Kostkontrakan::class.java)
            kostKontrakan.putExtra("kategorikostkontrakan", str)
            activity.startActivity(kostKontrakan)
        }
    }
}