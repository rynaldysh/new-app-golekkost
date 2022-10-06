package com.mavendra.golekkostv3.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.rajaongkir.Costs
import kotlin.collections.ArrayList

class KurirAdapter(var data:ArrayList<Costs>, var kurir: String, var listener: Listeners):RecyclerView.Adapter<KurirAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tvNamaPengiriman)
        val tvHari = view.findViewById<TextView>(R.id.tvLamaPengiriman)
        val tvHarga = view.findViewById<TextView>(R.id.tvHargaPengiriman)
        val tvBerat = view.findViewById<TextView>(R.id.tvBeratPengiriman)
        val rb = view.findViewById<RadioButton>(R.id.rbPengiriman)
        val layout = view.findViewById<LinearLayout>(R.id.layoutPengiriman)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_kurir, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        holder.rb.isChecked = a.isActive
        holder.tvName.text = kurir + "" + a.service
        val cost = a.cost[0]
        holder.tvHari.text = cost.etd + " hari Kerja"
        holder.tvHarga.text = Helper().gantiRupiah(cost.value)
        holder.tvBerat.text = "1 Kg x " + Helper().gantiRupiah(cost.value)
        /*holder.tvAlamat.text = a.alamat + ", " + a.kecamatan + ", " + a.kodepos + ", (" + a.type + ")"*/

        holder.rb.setOnClickListener {
            a.isActive = true
            listener.onClicked(a, holder.adapterPosition)
        }

        /*holder.layout.setOnClickListener {
            a.isSelected = true
            listener.onClicked(a)
        }*/
    }

    interface  Listeners{
        fun onClicked(data:Costs, index: Int)
    }
}