package com.mavendra.golekkostv3.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.app.Constants
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.DetailPesanJasa
import com.mavendra.golekkostv3.model.DetailTransaksi
import com.mavendra.golekkostv3.model.PesanJasa
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class JasaangkutPesanAdapter(var data:ArrayList<DetailPesanJasa>):RecyclerView.Adapter<JasaangkutPesanAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){

        val tvName = view.findViewById<TextView>(R.id.tvNamaDetailPesanJasa)
        val ivItem = view.findViewById<ImageView>(R.id.ivItemDetailPesanJasa)
        val tvHarga = view.findViewById<TextView>(R.id.tvHargaDetailPesanJasa)
        val tvLokasi = view.findViewById<TextView>(R.id.tvLokasiDetailPesanJasa)
        val layout = view.findViewById<CardView>(R.id.layoutDetailTransfer)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_pesan_jasa, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]
        val b = a.jasaangkut

        holder.tvName.text = b.name
        holder.tvHarga.text = Helper().gantiRupiah(b.harga)
        holder.tvLokasi.text = b.lokasi

        val image =  Constants.JASAANGKUT_URL + b.image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.beranda_ex_kostt)
            .error(R.drawable.beranda_ex_kostt)
            .into(holder.ivItem)

        /*holder.layout.setOnClickListener {
            *//*listener.onClicked(a)*//*
        }*/
    }

    interface  Listeners{
        fun onClicked(data: DetailPesanJasa)
    }
}