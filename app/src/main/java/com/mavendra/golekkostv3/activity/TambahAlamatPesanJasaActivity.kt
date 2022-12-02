package com.mavendra.golekkostv3.activity

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.AlamatPesanJasa
import com.mavendra.golekkostv3.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_tambah_alamat_pesan_jasa.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import kotlinx.android.synthetic.main.toolbar_custom_bottom_tambah_alamat_pesan_jasa.*
import java.text.SimpleDateFormat
import java.util.*


class TambahAlamatPesanJasaActivity : AppCompatActivity() {

    private lateinit var et: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_alamat_pesan_jasa)

        Helper().setToolbar(this, toolbarBiasa, "Tambah alamat pemesanan")
        mainButton()

    }

    private fun mainButton() {
        btSimpanPesanJasa.setOnClickListener {
            simpan()
        }

        ibPilihtanggalPesanJasa.setOnClickListener {
            setTanggal()
        }
    }

    val myCalendar = Calendar.getInstance()
    val datePicker = DatePickerDialog.OnDateSetListener{ view, year, month,
                                                         dayofmonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, month)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayofmonth)
        updateable(myCalendar)
    }

    private fun setTanggal(){
        DatePickerDialog(this, datePicker,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateable(mycalendar: Calendar){
        val myFormat = "EEEE, dd MMM yyyy"
        val sdf = SimpleDateFormat(myFormat,Locale("in", "ID"))

        et = findViewById(R.id.etTanggalPindahanPesanJasa)
        et.setText(sdf.format(mycalendar.time))
    }



    private fun simpan(){
        when {
            etNamaPesanJasa.text.isEmpty() -> {
                error(etNamaPesanJasa)
                return
            }
            etPhonePesanJasa.text.isEmpty() -> {
                error(etPhonePesanJasa)
                return
            }
            etAlamatAsalPesanJasa.text.isEmpty() -> {
                error(etAlamatAsalPesanJasa)
                return
            }
            etAlamatTujuanPesanJasa.text.isEmpty() -> {
                error(etAlamatTujuanPesanJasa)
                return
            }
            etTypeAsalPesanJasa.text.isEmpty() -> {
                error(etTypeAsalPesanJasa)
                return
            }
            etTypeTujuanPesanJasa.text.isEmpty() -> {
                error(etTypeTujuanPesanJasa)
                return
            }
            etTanggalPindahanPesanJasa.text.isEmpty() -> {
                error(etTanggalPindahanPesanJasa)
                return
            }
        }

        val alamat = AlamatPesanJasa()
        alamat.name = etNamaPesanJasa.text.toString()
        alamat.phone = etPhonePesanJasa.text.toString()
        alamat.detail_lokasi_asal = etAlamatAsalPesanJasa.text.toString()
        alamat.detail_lokasi_tujuan = etAlamatTujuanPesanJasa.text.toString()
        alamat.type_asal = etTypeAsalPesanJasa.text.toString()
        alamat.type_tujuan = etTypeTujuanPesanJasa.text.toString()
        alamat.tanggal = etTanggalPindahanPesanJasa.text.toString()

        insert(alamat)
    }

    fun error(editText: EditText){
        editText.error = "Kolom tidak boleh kosong"
        editText.requestFocus()
    }

    fun toast(string: String){
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    fun insert(data: AlamatPesanJasa){
        val myDb = MyDatabase.getInstance(this)!!
        if (myDb.daoAlamatPesanJasa().getByStatus(true) == null){
            data.isSelected = true
        }
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamatPesanJasa().insert(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                toast("Insert Data berhasil")
                onBackPressed()
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}