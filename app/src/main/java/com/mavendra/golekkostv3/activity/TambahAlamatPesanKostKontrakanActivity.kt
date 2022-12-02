package com.mavendra.golekkostv3.activity

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.AlamatPesanKostKontrakan
import com.mavendra.golekkostv3.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_tambah_alamat_pesan_kost_kontrakan.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import kotlinx.android.synthetic.main.toolbar_custom_bottom_tambah_alamat_pesan_kost.*
import java.text.SimpleDateFormat
import java.util.*


class TambahAlamatPesanKostKontrakanActivity : AppCompatActivity() {

    private lateinit var et: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_alamat_pesan_kost_kontrakan)

        Helper().setToolbar(this, toolbarBiasa, "Tambah alamat pemesanan")
        mainButton()

    }

    private fun mainButton() {
        btSimpanPesanKostKontrakan.setOnClickListener {
            simpan()
        }

        btPilihtanggalPesanKostKontrakan.setOnClickListener {
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

        et = findViewById(R.id.etTanggalPindahanPesanKostKontrakan)
        et.setText(sdf.format(mycalendar.time))
    }



    private fun simpan(){
        when {
            etNamaPesanKostKontrakan.text.isEmpty() -> {
                error(etNamaPesanKostKontrakan)
                return
            }
            etPhonePesanKostKontrakan.text.isEmpty() -> {
                error(etPhonePesanKostKontrakan)
                return
            }
            etAlamatPesanKostKontrakan.text.isEmpty() -> {
                error(etAlamatPesanKostKontrakan)
                return
            }
            etTanggalPindahanPesanKostKontrakan.text.isEmpty() -> {
                error(etTanggalPindahanPesanKostKontrakan)
                return
            }
        }

        val alamat = AlamatPesanKostKontrakan()
        alamat.name = etNamaPesanKostKontrakan.text.toString()
        alamat.phone = etPhonePesanKostKontrakan.text.toString()
        alamat.alamat = etAlamatPesanKostKontrakan.text.toString()
        alamat.tanggal = etTanggalPindahanPesanKostKontrakan.text.toString()

        insert(alamat)
    }

    fun error(editText: EditText){
        editText.error = "Kolom tidak boleh kosong"
        editText.requestFocus()
    }

    fun toast(string: String){
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    fun insert(data: AlamatPesanKostKontrakan){
        val myDb = MyDatabase.getInstance(this)!!
        if (myDb.daoAlamatPesanKostKontrakan().getByStatus(true) == null){
            data.isSelected = true
        }
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamatPesanKostKontrakan().insert(data) }
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