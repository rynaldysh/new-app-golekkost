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
import com.mavendra.golekkostv3.model.Alamat
import kotlin.collections.ArrayList

class AlamatAdapter(var data:ArrayList<Alamat>, var listener: Listeners):RecyclerView.Adapter<AlamatAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tvNamaAlamat)
        val tvPhone = view.findViewById<TextView>(R.id.tvPhoneAlamat)
        val tvAlamat = view.findViewById<TextView>(R.id.tvAlamatAlamat)
        val layout = view.findViewById<CardView>(R.id.layoutAlamat)
        val rb = view.findViewById<RadioButton>(R.id.rbAlamat)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_alamat, parent, false)
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
        holder.tvAlamat.text = a.alamat + ", " + a.kota + ", " + a.kodepos + ", (" + a.type + ")"

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
        fun onClicked(data:Alamat)
    }
}