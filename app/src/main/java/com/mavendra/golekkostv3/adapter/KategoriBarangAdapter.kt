package com.mavendra.golekkostv3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.app.Constants
import com.mavendra.golekkostv3.model.Barang
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class KategoriBarangAdapter(var data: ArrayList<Barang>,  var listener: Listeners):RecyclerView.Adapter<KategoriBarangAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tvNamaKategoriBarang)
        val tvHarga = view.findViewById<TextView>(R.id.tvHargaKategoriBarang)
        val tvLokasi = view.findViewById<TextView>(R.id.tvLokasiKategoriBarang)
        val tvDeskripsi = view.findViewById<TextView>(R.id.tvDeskripsiKategoriBarang)
        val ivBarang = view.findViewById<ImageView>(R.id.ivKategoriBarang)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatusKategoriBarang)
        val layout = view.findViewById<CardView>(R.id.layoutKategoriBarang)


    }

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_kategori_barang, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvName.text = data[position].name
        holder.tvHarga.text = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(Integer.valueOf(data[position].harga))
        holder.tvLokasi.text = data[position].lokasi
        holder.tvDeskripsi.text = data[position].deskripsi
        holder.tvStatus.text = data[position].status

        var color = context.getColor(R.color.terjual)

        when (data[position].status) {
            "TERSEDIA" -> color = context.getColor(R.color.tersedia)
            "HABIS" -> color = context.getColor(R.color.terjual)
        }

        holder.tvStatus.setBackgroundColor(color)

        val image =  Constants.barang_url + data[position].image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.beranda_ex_kostt)
            .error(R.drawable.beranda_ex_kostt)
            .into(holder.ivBarang)

        holder.layout.setOnClickListener {
            listener.onClicked(data[position])
        }
    }

    interface  Listeners{
        fun onClicked(data: Barang)
    }
}