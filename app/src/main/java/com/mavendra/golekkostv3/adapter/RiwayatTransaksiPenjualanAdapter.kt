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
import com.mavendra.golekkostv3.model.Transaksi
import kotlin.collections.ArrayList

class RiwayatTransaksiPenjualanAdapter(var data:ArrayList<Transaksi>, var listener: Listeners):RecyclerView.Adapter<RiwayatTransaksiPenjualanAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tvNamaRiwayatTransaksiPenjualan)
        val tvHarga = view.findViewById<TextView>(R.id.tvHargaRiwayatTransaksiPenjualan)
        val tvTanggal = view.findViewById<TextView>(R.id.tvTanggalRiwayatTransaksiPenjualan)
        val tvItem = view.findViewById<TextView>(R.id.tvTotalItemRiwayatTransaksiPenjualan)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatusRiwayatTransaksiPenjualan)
        /*val tvDetail = view.findViewById<TextView>(R.id.tvDetailRiwayat)*/
        val layout = view.findViewById<CardView>(R.id.layoutRiwayatTransaksiPenjualan)

    }

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat_transaksi_penjualan, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        /*val name = a.details[0].barang.name*/
        /*val name = a.details[0].barang.name*/

        holder.tvName.text = a.name
        holder.tvHarga.text = Helper().gantiRupiah(a.total_transfer)
        holder.tvItem.text = a.total_item + " Item"

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
        var color = context.getColor(R.color.menunggu)
        if (a.status == "SELESAI") color = context.getColor(R.color.selesai)
        else if (a.status == "BATAL") color = context.getColor(R.color.batal)

        holder.tvStatus.setTextColor(color)

        holder.layout.setOnClickListener {
            listener.onClicked(a)
        }
    }

    interface  Listeners{
        fun onClicked(data: Transaksi)
    }
}