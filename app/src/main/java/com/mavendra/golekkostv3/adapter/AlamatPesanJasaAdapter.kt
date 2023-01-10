package com.mavendra.golekkostv3.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.model.AlamatPesanJasa
import com.mavendra.golekkostv3.model.Barang
import com.mavendra.golekkostv3.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

class AlamatPesanJasaAdapter(var data:ArrayList<AlamatPesanJasa>, var listener: Listeners):RecyclerView.Adapter<AlamatPesanJasaAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tvNamaAlamatPesanJasa)
        val tvPhone = view.findViewById<TextView>(R.id.tvPhoneAlamatPesanJasa)
        val tvAlamat = view.findViewById<TextView>(R.id.tvAlamatAlamatPesanJasa)
        val tvTanggal = view.findViewById<TextView>(R.id.tvTanggalAlamatPesanJasa)
        val layout = view.findViewById<CardView>(R.id.layoutAlamatPesanJasa)
        val rb = view.findViewById<RadioButton>(R.id.rbAlamatPesanJasa)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_alamat_pesan_jasa, parent, false)
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
        holder.tvAlamat.text = a.detail_lokasi_asal + ", (" + a.type_asal + ")"

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
        fun onClicked(data:AlamatPesanJasa)
    }
}