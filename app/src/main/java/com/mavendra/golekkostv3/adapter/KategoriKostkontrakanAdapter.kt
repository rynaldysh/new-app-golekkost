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
import com.mavendra.golekkostv3.model.Jasaangkut
import com.mavendra.golekkostv3.model.Kostkontrakan
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class KategoriKostkontrakanAdapter(var data: ArrayList<Kostkontrakan>, var listener: Listeners):RecyclerView.Adapter<KategoriKostkontrakanAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tvNamaKategoriKostkontrakan)
        val tvHarga = view.findViewById<TextView>(R.id.tvHargaKategoriKostkontrakan)
        val tvLokasi = view.findViewById<TextView>(R.id.tvLokasiKategoriKostkontrakan)
        val tvDeskripsi = view.findViewById<TextView>(R.id.tvDeskripsiKategoriKostkontrakan)
        val tvMayoritas = view.findViewById<TextView>(R.id.tvMayoritasKategoriKostkontrakan)
        val ivBarang = view.findViewById<ImageView>(R.id.ivKategoriKostkontrakan)
        val layout = view.findViewById<CardView>(R.id.layoutKategoriKostkontrakan)

    }

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_kategori_kost_kontrakan, parent, false)
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
        holder.tvMayoritas.text = data[position].mayoritas

        val image =  Constants.KOSTKONTRAKAN_URL + data[position].image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.beranda_ex_kostt)
            .error(R.drawable.beranda_ex_kostt)
            .into(holder.ivBarang)

        holder.layout.setOnClickListener {
            listener.onClicked(data[position])
        }

        var color = context.getColor(R.color.mahasiswa)
        when (data[position].mayoritas) {
            "Mahasiswa" -> color = context.getColor(R.color.mahasiswa)
            "Pegawai" -> color = context.getColor(R.color.pegawai)
            "Campuran" -> color = context.getColor(R.color.campuran)
        }

        holder.tvMayoritas.setBackgroundColor(color)
    }

    interface  Listeners{
        fun onClicked(data: Kostkontrakan)
    }
}