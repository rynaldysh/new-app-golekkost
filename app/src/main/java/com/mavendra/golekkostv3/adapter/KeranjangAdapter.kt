package com.mavendra.golekkostv3.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.app.Constants.KOSTKONTRAKAN_URL
import com.mavendra.golekkostv3.app.Constants.barang_url
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.Barang
import com.mavendra.golekkostv3.room.MyDatabase
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class KeranjangAdapter(var activity: Activity, var data:ArrayList<Barang>, var listener: Listeners):RecyclerView.Adapter<KeranjangAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tvNamaItem)
        val tvHarga = view.findViewById<TextView>(R.id.tvHargaItem)
        val tvLokasi = view.findViewById<TextView>(R.id.tvLokasiItem)
        val ivItem = view.findViewById<ImageView>(R.id.ivItem)
        val layout = view.findViewById<CardView>(R.id.layoutKeranjang)

        val ivTambah = view.findViewById<ImageView>(R.id.ivTambah)
        val ivKurang = view.findViewById<ImageView>(R.id.ivKurang)
        val ivDelete = view.findViewById<ImageView>(R.id.ivDelete)
        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        val tvJumlah = view.findViewById<TextView>(R.id.tvJumlahBarang)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val barang = data[position]
        val harga = Integer.valueOf(barang.harga)

        holder.tvName.text = barang.name
        holder.tvHarga.text = Helper().gantiRupiah(harga * barang.jumlah)
        holder.tvLokasi.text = barang.lokasi

        holder.checkBox.isChecked = barang.selected
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            barang.selected = isChecked
            update(barang)
        }

        var jumlah = barang.jumlah
        holder.tvJumlah.text = jumlah.toString()
        val image =  barang_url + barang.image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.beranda_ex_kostt)
            .error(R.drawable.beranda_ex_kostt)
            .into(holder.ivItem)

        holder.ivTambah.setOnClickListener {
            if (jumlah >= 10) return@setOnClickListener
            jumlah++

            barang.jumlah = jumlah
            update(barang)
            holder.tvJumlah.text = jumlah.toString()
            holder.tvHarga.text = Helper().gantiRupiah((harga * jumlah).toString())
        }

        holder.ivKurang.setOnClickListener {
            if (jumlah <= 1) return@setOnClickListener
            jumlah--

            barang.jumlah = jumlah
            update(barang)
            holder.tvJumlah.text = jumlah.toString()
            holder.tvHarga.text = Helper().gantiRupiah((harga * jumlah).toString())
        }

        holder.ivDelete.setOnClickListener {
            delete(barang)
            listener.onDelete(position)

        }
    }

    interface Listeners{
        fun onUpdate()
        fun onDelete(position: Int)
    }

    fun update(data: Barang){
        val mydDb: MyDatabase = MyDatabase.getInstance(activity)!!
        CompositeDisposable().add(Observable.fromCallable { mydDb.daoKeranjang().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listener.onUpdate()
            })
    }

    fun delete(data: Barang){
        val mydDb: MyDatabase = MyDatabase.getInstance(activity)!!
        CompositeDisposable().add(Observable.fromCallable { mydDb.daoKeranjang().delete(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

            })
    }
}