package com.mavendra.golekkostv3.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.Barang
import kotlin.collections.ArrayList

class RiwayatJualBarangAdapter(var data:ArrayList<Barang>, var listener: Listeners):RecyclerView.Adapter<RiwayatJualBarangAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tvNamaRiwayatBarangJual)
        val tvHarga = view.findViewById<TextView>(R.id.tvHargaRiwayatBarangJual)
        val tvTanggal = view.findViewById<TextView>(R.id.tvTanggalRiwayatBarangJual)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatusRiwayatBarangJual)
        /*val tvDetail = view.findViewById<TextView>(R.id.tvDetailRiwayat)*/
        val layout = view.findViewById<CardView>(R.id.layoutRiwayatBarangJual)

    }

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat_jual_barang, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        /*val name = a.details[0].barang.name*/

        holder.tvName.text = a.name
        holder.tvHarga.text = Helper().gantiRupiah(a.harga)


        /*holder.tvDetail.text = a.name*/
        holder.tvStatus.text = a.status

        //new format date
        val newFormat = "d MMM yyyy"
        val oldFormat = "yyyy-MM-dd kk:mm:ss"

        /*val dateFormat = SimpleDateFormat(oldFormat)
        val dateConvert = dateFormat.parse(a.created_att)
        dateFormat.applyPattern(newFormat)
        val newDate = dateFormat.format(dateConvert)*/

        holder.tvTanggal.text = Helper().convertDate(a.created_att, newFormat)

        //change color status
        var color = context.getColor(R.color.terjual)
        if (a.status == "TERSEDIA") color = context.getColor(R.color.tersedia)
        else if (a.status == "HABIS") color = context.getColor(R.color.terjual)

        holder.tvStatus.setTextColor(color)

        holder.layout.setOnClickListener {
            listener.onClicked(a)
        }
    }

    interface  Listeners{
        fun onClicked(data: Barang)
    }
}