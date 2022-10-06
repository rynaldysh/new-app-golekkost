package com.mavendra.golekkostv3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.app.Constants.JASAANGKUT_URL
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.Barang
import com.mavendra.golekkostv3.model.Jasaangkut
import com.mavendra.golekkostv3.room.MyDatabase
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_jasaangkut.*
import kotlinx.android.synthetic.main.activity_detail_jasaangkut.tvDeskripsi
import kotlinx.android.synthetic.main.activity_detail_jasaangkut.tvLokasi
import kotlinx.android.synthetic.main.toolbar_custom_bottom_jasa_detail.*
import kotlinx.android.synthetic.main.toolbar_custom_top_jasa_detail.*

class DetailJasaAngkutActivity : AppCompatActivity() {

    var jasaangkut = Jasaangkut()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_jasaangkut)

        val json = intent.getStringExtra("kategorijasaangkut")
        jasaangkut = Gson().fromJson(json, Jasaangkut::class.java)

        getInfo()
        mainButton()
        checkSimpanJasa()

    }

    private fun mainButton() {
        ivSaveJasa.setOnClickListener {
            val mydDb: MyDatabase = MyDatabase.getInstance(this)!!
            val data = mydDb.daoSimpanJasa().getJasaangkut(jasaangkut.id)
            if (data == null){
                insert()
            } else {
                jasaangkut.jumlah =+ 1
                update(data)
            }
        }

        rlSimpanJasa.setOnClickListener {
            val intent = Intent("event:simpanjasa")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            onBackPressed()
        }
    }

    private fun insert(){
        val mydDb: MyDatabase = MyDatabase.getInstance(this)!!
        CompositeDisposable().add(Observable.fromCallable { mydDb.daoSimpanJasa().insert(jasaangkut) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkSimpanJasa()
                Log.d("respon", "Jasa berhasil disimpan")
                Toast.makeText(this, "Jasa berhasil disimpan", Toast.LENGTH_SHORT).show()
            })
    }

    fun update(data: Jasaangkut){
        val mydDb: MyDatabase = MyDatabase.getInstance(this)!!
        CompositeDisposable().add(Observable.fromCallable { mydDb.daoSimpanJasa().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkSimpanJasa()
                Log.d("respon", "Jasa sudah disimpan")
                Toast.makeText(this, "Jasa sudah disimpan", Toast.LENGTH_SHORT).show()
            })
    }

    private fun checkSimpanJasa(){
        val myDb: MyDatabase = MyDatabase.getInstance(this)!!
        val dataSimpanJasa = myDb.daoSimpanJasa().getAll()
        if(dataSimpanJasa.isNotEmpty()){
            div_angkaSimpanJasa.visibility = View.VISIBLE
            tv_angkaSimpanJasa.text = dataSimpanJasa.size.toString()

        } else {
            div_angkaSimpanJasa.visibility = View.GONE
        }
    }

    private fun getInfo() {

        //set Value
        tvNamaJasa.text = jasaangkut.name
        tvHargaJasa.text = Helper().gantiRupiah(jasaangkut.harga)
        tvLokasi.text = jasaangkut.lokasi
        tvDeskripsi.text = jasaangkut.deskripsi

        val image = JASAANGKUT_URL + jasaangkut.image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.beranda_ex_kostt)
            .error(R.drawable.beranda_ex_kostt)
            .into(ivgambarJasaangkut)

        Helper().setToolbar(this, toolbarDisimpanJasaAtas, jasaangkut.name)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
