package com.mavendra.golekkostv3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.app.Constants.KOSTKONTRAKAN_URL
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.Barang
import com.mavendra.golekkostv3.model.Kostkontrakan
import com.mavendra.golekkostv3.room.MyDatabase
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_kostkontrakan.*
import kotlinx.android.synthetic.main.toolbar_custom_bottom_jasa_detail.*
import kotlinx.android.synthetic.main.toolbar_custom_bottom_kost_kontrakan_detail.*
import kotlinx.android.synthetic.main.toolbar_custom_top_kost_kontrakan_detail.*

class DetailKostKontrakanActivity : AppCompatActivity() {

    var kostkontrakan = Kostkontrakan()

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kostkontrakan)

        val json = intent.getStringExtra("kategorikostkontrakan")
        kostkontrakan = Gson().fromJson(json, Kostkontrakan::class.java)

        buttonWa(kostkontrakan)
        getInfo(kostkontrakan)
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

    private val URL = "https://wa.me/"
    private fun buttonWa(kostkontrakan: Kostkontrakan){
        val ivWhatsapp: ImageView = findViewById(R.id.ivWhatsappKostkontrakan)
        ivWhatsapp.setOnClickListener {

            /*val webIntent: Intent = Uri.parse("https://wa.me/6282138125561").let { webpage ->
                Intent(Intent.ACTION_VIEW, webpage)
            }
            startActivity(webIntent)*/

            //------------------------//
            webView = findViewById(R.id.webKostKontrakan)

            webView.apply {
                loadUrl(URL + kostkontrakan.nomortelfon)
            }


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

    private fun getInfo(kostkontrakan: Kostkontrakan) {

        //set Value
        tvNamaKostDetail.text = kostkontrakan.name
        tvNamaPengelolaDetail.text = kostkontrakan.pengelola
        tvHargaKostDetail.text = Helper().gantiRupiah(kostkontrakan.harga)
        tvRasioPembayaranKostDetail.text = kostkontrakan.rasiobayar
        tvLokasiDetail.text = kostkontrakan.lokasi
        tvSisaKamarDetail.text = kostkontrakan.sisakamar
        tvTotalKamarDetail.text = kostkontrakan.totalkamar
        tvMayoritasDetail.text = kostkontrakan.mayoritas
        tvDeskripsiDetail.text = kostkontrakan.deskripsi
        tvListrikDetail.text = kostkontrakan.listrik
        tvAirDetail.text = kostkontrakan.air
        tvWifiDetail.text = kostkontrakan.wifi
        tvBedDetail.text = kostkontrakan.bed
        tvAcDetail.text = kostkontrakan.ac
        tvKamarMandiDetail.text = kostkontrakan.kamarmandi
        tvKlosetDetail.text = kostkontrakan.kloset
        tvSatpamDetail.text = kostkontrakan.satpam

        var color = getColor(R.color.mahasiswa)
        when (kostkontrakan.mayoritas) {
            "Mahasiswa" -> color = getColor(R.color.mahasiswa)
            "Pegawai" -> color = getColor(R.color.pegawai)
            "Campuran" -> color = getColor(R.color.campuran)
        }

        tvMayoritasDetail.setBackgroundColor(color)

        val image =  KOSTKONTRAKAN_URL + kostkontrakan.image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.beranda_ex_kostt)
            .error(R.drawable.beranda_ex_kostt)
            .into(ivGambarKostDetail)

        Helper().setToolbar(this, toolbarDisimpanKostKontrakanAtas, kostkontrakan.name)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


}
