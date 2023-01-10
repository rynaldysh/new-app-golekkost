package com.mavendra.golekkostv3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.AlamatPesanKostKontrakanAdapter
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.AlamatPesanKostKontrakan
import com.mavendra.golekkostv3.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list_alamat_pesan_kost_kontrakan.*
import kotlinx.android.synthetic.main.toolbar_biasa.*


class ListAlamatPesanKostKontrakanActivity : AppCompatActivity() {
    lateinit var myDb: MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_alamat_pesan_kost_kontrakan)
        Helper().setToolbar(this, toolbarBiasa, "Pilih Alamat")
        myDb = MyDatabase.getInstance(this)!!

        mainButton()

    }

    private fun displayAlamat() {
        val arrayList = myDb.daoAlamatPesanKostKontrakan().getAll() as ArrayList

        if (arrayList.isEmpty()) tvKosongPesanKostKontrakan.visibility = View.VISIBLE
        else tvKosongPesanKostKontrakan.visibility = View.GONE

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvAlamatPesanKostKontrakan.adapter = AlamatPesanKostKontrakanAdapter(arrayList, object : AlamatPesanKostKontrakanAdapter.Listeners {
            override fun onClicked(data: AlamatPesanKostKontrakan) {
                if (myDb.daoAlamatPesanKostKontrakan().getByStatus(true) != null){
                    val alamatActive = myDb.daoAlamatPesanKostKontrakan().getByStatus(true)!!
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
        rvAlamatPesanKostKontrakan.layoutManager = layoutManager
    }

    private fun updateActive(dataActive: AlamatPesanKostKontrakan, dataNonActive: AlamatPesanKostKontrakan){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamatPesanKostKontrakan().update(dataActive) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                updateNonActive(dataNonActive)
            })
    }

    private fun updateNonActive(data: AlamatPesanKostKontrakan){
        data.isSelected = true
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamatPesanKostKontrakan().update(data) }
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
        btBuatAlamatPesanKostKontrakan.setOnClickListener {
            startActivity(Intent(this, TambahAlamatPesanKostKontrakanActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}