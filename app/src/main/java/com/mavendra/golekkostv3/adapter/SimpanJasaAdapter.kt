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
import com.mavendra.golekkostv3.model.Jasaangkut
import com.mavendra.golekkostv3.room.MyDatabase
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class SimpanJasaAdapter(var activity: Activity, var data:ArrayList<Jasaangkut>, var listener: Listeners):RecyclerView.Adapter<SimpanJasaAdapter.Holder>(){

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tvNamaItemDisimpanJasa)
        val tvHarga = view.findViewById<TextView>(R.id.tvHargaItemDisimpanJasa)
        val tvLokasi = view.findViewById<TextView>(R.id.tvLokasiItemDisimpanJasa)
        /*val tvMayoritas = view.findViewById<TextView>(R.id.tvMayoritas)*/
        val ivItem = view.findViewById<ImageView>(R.id.ivItemDisimpanJasa)
        val layout = view.findViewById<CardView>(R.id.layoutDisimpanJasa)

        val ivDelete = view.findViewById<ImageView>(R.id.ivDeleteItemDisimpanJasa)
        val checkBox = view.findViewById<CheckBox>(R.id.checkBoxItemDisimpanJasa)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_disimpan_jasa, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val jasa = data[position]
        val harga = Integer.valueOf(jasa.harga)

        holder.tvName.text = jasa.name
        holder.tvHarga.text = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(harga)
        holder.tvLokasi.text = jasa.lokasi

        holder.checkBox.isChecked = jasa.selected
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            jasa.selected = isChecked
            update(jasa)
        }

        val image =  JASAANGKUT_URL + jasa.image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.beranda_ex_kostt)
            .error(R.drawable.beranda_ex_kostt)
            .into(holder.ivItem)

        holder.ivDelete.setOnClickListener {
            delete(jasa)
            listener.onDelete(position)

        }
    }

    interface Listeners{
        fun onUpdate()
        fun onDelete(position: Int)
    }

    fun update(data: Jasaangkut){
        val mydDb: MyDatabase = MyDatabase.getInstance(activity)!!
        CompositeDisposable().add(Observable.fromCallable { mydDb.daoSimpanJasa().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listener.onUpdate()
            })
    }

    fun delete(data: Jasaangkut){
        val mydDb: MyDatabase = MyDatabase.getInstance(activity)!!
        CompositeDisposable().add(Observable.fromCallable { mydDb.daoSimpanJasa().delete(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

            })
    }
}