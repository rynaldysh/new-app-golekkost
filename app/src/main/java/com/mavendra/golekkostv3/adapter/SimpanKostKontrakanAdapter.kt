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
import com.mavendra.golekkostv3.app.Constants.JASAANGKUT_URL
import com.mavendra.golekkostv3.app.Constants.KOSTKONTRAKAN_URL
import com.mavendra.golekkostv3.model.Kostkontrakan
import com.mavendra.golekkostv3.room.MyDatabase
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class SimpanKostKontrakanAdapter(var activity: Activity, var data:ArrayList<Kostkontrakan>, var listener: Listeners):RecyclerView.Adapter<SimpanKostKontrakanAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tvNamaItemDisimpanKostKontrakan)
        val tvHarga = view.findViewById<TextView>(R.id.tvHargaItemDisimpanKostKontrakan)
        val tvLokasi = view.findViewById<TextView>(R.id.tvLokasiItemDisimpanKostKontrakan)
        /*val tvMayoritas = view.findViewById<TextView>(R.id.tvMayoritas)*/
        val ivItem = view.findViewById<ImageView>(R.id.ivItemDisimpanKostKontrakan)
        val layout = view.findViewById<CardView>(R.id.layoutDisimpanKostKontrakan)

        val ivDelete = view.findViewById<ImageView>(R.id.ivDeleteItemDisimpanKostKontrakan)
        val checkBox = view.findViewById<CheckBox>(R.id.checkBoxItemDisimpanKostKontrakan)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_disimpan_kost_kontrakan, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val kostkontrakan = data[position]
        val harga = Integer.valueOf(kostkontrakan.harga)

        holder.tvName.text = kostkontrakan.name
        holder.tvHarga.text = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(harga)
        holder.tvLokasi.text = kostkontrakan.lokasi

        holder.checkBox.isChecked = kostkontrakan.selected
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            kostkontrakan.selected = isChecked
            update(kostkontrakan)
        }

        val image =  KOSTKONTRAKAN_URL + kostkontrakan.image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.beranda_ex_kostt)
            .error(R.drawable.beranda_ex_kostt)
            .into(holder.ivItem)

        holder.ivDelete.setOnClickListener {
            delete(kostkontrakan)
            listener.onDelete(position)

        }
    }

    interface Listeners{
        fun onUpdate()
        fun onDelete(position: Int)
    }

    fun update(data: Kostkontrakan){
        val mydDb: MyDatabase = MyDatabase.getInstance(activity)!!
        CompositeDisposable().add(Observable.fromCallable { mydDb.daoSimpanKostKontrakan().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listener.onUpdate()
            })
    }

    fun delete(data: Kostkontrakan){
        val mydDb: MyDatabase = MyDatabase.getInstance(activity)!!
        CompositeDisposable().add(Observable.fromCallable { mydDb.daoSimpanKostKontrakan().delete(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

            })
    }
}