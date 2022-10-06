package com.mavendra.golekkostv3.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.model.AlamatPesanKostKontrakan
import kotlin.collections.ArrayList

class AlamatPesanKostKontrakanAdapter(var data:ArrayList<AlamatPesanKostKontrakan>, var listener: Listeners):RecyclerView.Adapter<AlamatPesanKostKontrakanAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tvNamaAlamatPesanKostKontrakan)
        val tvPhone = view.findViewById<TextView>(R.id.tvPhoneAlamatPesanKostKontrakan)
        val tvAlamat = view.findViewById<TextView>(R.id.tvAlamatAlamatPesanKostKontrakan)
        val tvTanggal = view.findViewById<TextView>(R.id.tvTanggalAlamatPesanKostKontrakan)
        val layout = view.findViewById<CardView>(R.id.layoutAlamatPesanKostKontrakan)
        val rb = view.findViewById<RadioButton>(R.id.rbAlamatPesanKostKontrakan)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_alamat_pesan_kost_kontrakan, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        holder.rb.isChecked = a.isSelected
        holder.tvName.text = a.name
        holder.tvPhone.text = a.phone
        holder.tvTanggal.text = a.tanggal
        holder.tvAlamat.text = a.alamat

        holder.rb.setOnClickListener {
            a.isSelected = true
            listener.onClicked(a)
        }

        holder.layout.setOnClickListener {
            a.isSelected = true
            listener.onClicked(a)
        }
    }

    interface  Listeners{
        fun onClicked(data:AlamatPesanKostKontrakan)
    }
}