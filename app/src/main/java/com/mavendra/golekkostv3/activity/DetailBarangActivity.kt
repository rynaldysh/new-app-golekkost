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
import com.mavendra.golekkostv3.app.Constants.barang_url
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.Barang
import com.mavendra.golekkostv3.room.MyDatabase
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_barang.*
import kotlinx.android.synthetic.main.toolbar_custom_bottom_barang_detail.*
import kotlinx.android.synthetic.main.toolbar_custom_keranjang_detail.*

class DetailBarangActivity : AppCompatActivity() {

    var barang = Barang()

    private lateinit var webView: WebView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_barang)

        val json = intent.getStringExtra("kategoribarang")
        barang = Gson().fromJson(json, Barang::class.java)

        getInfo(barang)
        mainButton()
        buttonWa(barang)
        checkKeranjang()
        Helper().setToolbar(this, toolbarKeranjangAtas, barang.name)

    }

    private fun mainButton() {
        ivKeranjang.setOnClickListener {
            val mydDb: MyDatabase = MyDatabase.getInstance(this)!!
            val data = mydDb.daoKeranjang().getBarang(barang.id)
            if (data == null){
                insert()
            } else {
                barang.jumlah =+ 1
                update(data)
            }

        }

        rlKeranjang.setOnClickListener {
            val intent = Intent("event:keranjang")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            onBackPressed()
        }
    }
    private val URL = "https://wa.me/"
    private fun buttonWa(barang: Barang){
        val ivWhatsapp: ImageView = findViewById(R.id.ivWhatsapp)
        ivWhatsapp.setOnClickListener {

            /*val webIntent: Intent = Uri.parse("https://wa.me/6282138125561").let { webpage ->
                Intent(Intent.ACTION_VIEW, webpage)
            }
            startActivity(webIntent)*/

            //------------------------//
            webView = findViewById(R.id.web)

            webView.apply {
                loadUrl(URL + barang.notelfon)
            }


        }
    }

    fun insert(){
        val mydDb: MyDatabase = MyDatabase.getInstance(this)!!
        CompositeDisposable().add(Observable.fromCallable { mydDb.daoKeranjang().insert(barang) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkKeranjang()
                Log.d("respon", "data masuk")
                Toast.makeText(this, "Berhasil menambahkan ke keranjang", Toast.LENGTH_SHORT).show()
            })
    }

    fun update(data: Barang){
        val mydDb: MyDatabase = MyDatabase.getInstance(this)!!
        CompositeDisposable().add(Observable.fromCallable { mydDb.daoKeranjang().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkKeranjang()
                Log.d("respon", "data masuk")
                Toast.makeText(this, "Berhasil menambahkan ke keranjang", Toast.LENGTH_SHORT).show()
            })
    }

    private fun checkKeranjang(){
        val myDb: MyDatabase = MyDatabase.getInstance(this)!!
        val dataKeranjang = myDb.daoKeranjang().getAll()
        if(dataKeranjang.isNotEmpty()){
            div_angka.visibility = View.VISIBLE
            tv_angka.text = dataKeranjang.size.toString()

        } else {
            div_angka.visibility = View.GONE
        }
    }

    fun getInfo(barang: Barang) {

        //set Value
        tvNamaBarang.text = barang.name
        tvNamaPemilik.text = barang.name_pemilik
        tvHargaBarang.text = Helper().gantiRupiah(barang.harga)
        tvLokasiBarang.text = barang.lokasi
        tvDeskripsiBarang.text = barang.deskripsi


        val image =  barang_url + barang.image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.beranda_ex_kostt)
            .error(R.drawable.beranda_ex_kostt)
            .into(ivgambarBarang)

        Helper().setToolbar(this, toolbarKeranjangAtas, barang.name)
    }

    fun checkKeterdediaan(){
        if(barang.status != "TERSEDIA"){
            ivKeranjang.visibility = View.GONE
        } else {
            ivKeranjang.visibility = View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        checkKeterdediaan()
        super.onResume()
    }


}
