package com.mavendra.golekkostv3.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.app.Constants.KOSTKONTRAKAN_URL
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.Kostkontrakan
import com.mavendra.golekkostv3.room.MyDatabase
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_kostkontrakan.*
import kotlinx.android.synthetic.main.activity_detail_kostkontrakan.tvDeskripsi
import kotlinx.android.synthetic.main.activity_detail_kostkontrakan.tvLokasi
import kotlinx.android.synthetic.main.toolbar_custom_bottom_kost_kontrakan_detail.*
import kotlinx.android.synthetic.main.toolbar_custom_top_kost_kontrakan_detail.*

class DetailKostKontrakanActivity : AppCompatActivity() {

    lateinit var kostkontrakan: Kostkontrakan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kostkontrakan)

        getInfo()
        mainButton()
        checkSimpanKostKontrakan()
    }

    private fun mainButton() {
        ivSaveKostkontrakan.setOnClickListener {
            val mydDb: MyDatabase = MyDatabase.getInstance(this)!!
            val data = mydDb.daoSimpanKostKontrakan().getKostkostkontrakan(kostkontrakan.id)
            if (data == null){
                insert()
            } else {
                kostkontrakan.jumlah =+ 1
                update(data)
            }
        }

        rlSimpanKostKontrakan.setOnClickListener {
            val intent = Intent("event:simpankostkontrakan")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            onBackPressed()
        }
    }

    private fun insert(){
        val mydDb: MyDatabase = MyDatabase.getInstance(this)!!
        CompositeDisposable().add(Observable.fromCallable { mydDb.daoSimpanKostKontrakan().insert(kostkontrakan) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkSimpanKostKontrakan()
                Log.d("respon", "Kost atau kontrakan berhasil disimpan")
                Toast.makeText(this, "Kost atau kontrakan berhasil disimpan", Toast.LENGTH_SHORT).show()
            })
    }

    fun update(data: Kostkontrakan){
        val mydDb: MyDatabase = MyDatabase.getInstance(this)!!
        CompositeDisposable().add(Observable.fromCallable { mydDb.daoSimpanKostKontrakan().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkSimpanKostKontrakan()
                Log.d("respon", "Kost atau kontrakan sudah disimpan")
                Toast.makeText(this, "Kost atau kontrakan sudah disimpan", Toast.LENGTH_SHORT).show()
            })
    }
    private fun checkSimpanKostKontrakan(){
        val myDb: MyDatabase = MyDatabase.getInstance(this)!!
        val dataSimpanKostKontrakan = myDb.daoSimpanKostKontrakan().getAll()
        if(dataSimpanKostKontrakan.isNotEmpty()){
            div_angkaSimpanKostKontrakan.visibility = View.VISIBLE
            tv_angkaSimpanKostKontrakan.text = dataSimpanKostKontrakan.size.toString()

        } else {
            div_angkaSimpanKostKontrakan.visibility = View.GONE
        }
    }

    private fun getInfo() {
        val data = intent.getStringExtra("kostkontrakan")
        kostkontrakan = Gson().fromJson<Kostkontrakan>(data, Kostkontrakan::class.java)

        //set Value
        tvNamaKost.text = kostkontrakan.name
        tvNamaPengelola.text = kostkontrakan.pengelola
        tvHargaKost.text = Helper().gantiRupiah(kostkontrakan.harga)
        tvLokasi.text = kostkontrakan.lokasi
        tvSisaKamar.text = kostkontrakan.sisakamar
        tvTotalKamar.text = kostkontrakan.totalkamar
        tvMayoritas.text = kostkontrakan.mayoritas
        tvDeskripsi.text = kostkontrakan.deskripsi
        tvListrik.text = kostkontrakan.listrik
        tvAir.text = kostkontrakan.air
        tvWifi.text = kostkontrakan.wifi
        tvBed.text = kostkontrakan.bed
        tvAc.text = kostkontrakan.ac
        tvKamarMandi.text = kostkontrakan.kamarmandi
        tvKloset.text = kostkontrakan.kloset
        tvSatpam.text = kostkontrakan.satpam

        var color = getColor(R.color.mahasiswa)
        when (kostkontrakan.mayoritas) {
            "Mahasiswa" -> color = getColor(R.color.mahasiswa)
            "Pegawai" -> color = getColor(R.color.pegawai)
            "Campuran" -> color = getColor(R.color.campuran)
        }

        tvMayoritas.setBackgroundColor(color)

        val image =  KOSTKONTRAKAN_URL + kostkontrakan.image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.beranda_ex_kostt)
            .error(R.drawable.beranda_ex_kostt)
            .into(ivGambarKost)

        Helper().setToolbar(this, toolbarDisimpanKostKontrakanAtas, kostkontrakan.name)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


}
