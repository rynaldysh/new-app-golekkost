package com.mavendra.golekkostv3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.AlamatPesanJasaAdapter
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.AlamatPesanJasa
import com.mavendra.golekkostv3.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list_alamat_pesan_jasa.*
import kotlinx.android.synthetic.main.toolbar_biasa.*


class ListAlamatPesanJasaActivity : AppCompatActivity() {
    lateinit var myDb: MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_alamat_pesan_jasa)
        Helper().setToolbar(this, toolbarBiasa, "Pilih Alamat")
        myDb = MyDatabase.getInstance(this)!!

        mainButton()

    }

    private fun displayAlamat() {
        val arrayList = myDb.daoAlamatPesanJasa().getAll() as ArrayList

        if (arrayList.isEmpty()) tvKosongPesanJasa.visibility = View.VISIBLE
        else tvKosongPesanJasa.visibility = View.GONE

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvAlamatPesanJasa.adapter = AlamatPesanJasaAdapter(arrayList, object : AlamatPesanJasaAdapter.Listeners {
            override fun onClicked(data: AlamatPesanJasa) {
                if (myDb.daoAlamatPesanJasa().getByStatus(true) != null){
                    val alamatActive = myDb.daoAlamatPesanJasa().getByStatus(true)!!
                    alamatActive.isSelected = false
                    updateActive(alamatActive, data)

                    /*for (alamat in arrayList){
                        alamat.isSelected = false
                        update(alamat)
                    }*/
                }

                /*data.isSelected = true
                update(data)
                onBackPressed()*/
            }
        })
        rvAlamatPesanJasa.layoutManager = layoutManager
    }

    private fun updateActive(dataActive: AlamatPesanJasa, dataNonActive: AlamatPesanJasa){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamatPesanJasa().update(dataActive) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                updateNonActive(dataNonActive)
            })
    }

    private fun updateNonActive(data: AlamatPesanJasa){
        data.isSelected = true
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamatPesanJasa().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                onBackPressed()
            })
    }

    override fun onResume() {
        displayAlamat()
        super.onResume()
    }

    private fun mainButton() {
        btBuatAlamatPesanJasa.setOnClickListener {
            startActivity(Intent(this, TambahAlamatPesanJasaActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}