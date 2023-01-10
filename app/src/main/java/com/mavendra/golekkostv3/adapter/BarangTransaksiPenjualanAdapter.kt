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
import com.mavendra.golekkostv3.model.DetailTransaksi
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class BarangTransaksiPenjualanAdapter(var data:ArrayList<DetailTransaksi>):RecyclerView.Adapter<BarangTransaksiPenjualanAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){

        val tvName = view.findViewById<TextView>(R.id.tvNamaDetailTransaksiPenjualan)
        val tvItem = view.findViewById<TextView>(R.id.tvTotalItemDetailTransaksiPenjualan)
        val ivItem = view.findViewById<ImageView>(R.id.ivItemDetailTransaksiPenjualan)
        val tvHarga = view.findViewById<TextView>(R.id.tvHargaSatuanDetailTransaksiPenjualan)
        val tvHargaTotal = view.findViewById<TextView>(R.id.tvTotalHargaDetailTransaksiPenjualan)
        val layout = view.findViewById<CardView>(R.id.layoutDetailTransaksiPenjualan)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_barang_detail_transfer, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        val name = a.barang.name
        val b = a.barang

        holder.tvName.text = name
        holder.tvHarga.text = Helper().gantiRupiah(a.barang.harga)
        holder.tvHargaTotal.text = Helper().gantiRupiah(a.total_harga)
        holder.tvItem.text = a.total_item.toString() + " Item"

        val image =  Constants.barang_url + b.image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.beranda_ex_kostt)
            .error(R.drawable.beranda_ex_kostt)
            .into(holder.ivItem)

        holder.layout.setOnClickListener {
            /*listener.onClicked(a)*/
        }
    }

    interface  Listeners{
        fun onClicked(data: DetailTransaksi)
    }
}