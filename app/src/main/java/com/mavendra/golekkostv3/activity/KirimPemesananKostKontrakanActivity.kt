package com.mavendra.golekkostv3.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.model.*
import com.mavendra.golekkostv3.room.MyDatabase
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_kirim_pemesanan_jasa.*
import kotlinx.android.synthetic.main.activity_kirim_pemesanan_kost_kontrakan.*
import kotlinx.android.synthetic.main.activity_pengiriman.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class KirimPemesananKostKontrakanActivity : AppCompatActivity() {

    lateinit var myDb: MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kirim_pemesanan_kost_kontrakan)
        myDb = MyDatabase.getInstance(this)!!
        Helper().setToolbar(this, toolbarBiasa, "Pemesanan Kost Atau Kontrakan")

        mainButton()

    }

    @SuppressLint("SetTextI18n")
    fun checkAlamat(){
        if (myDb.daoAlamatPesanKostKontrakan().getByStatus(true) != null){
            cvAlamatDisimpanKostKontrakan.visibility = View.VISIBLE
            tvKosongDisimpanKostKontrakan.visibility = View.GONE

            val a = myDb.daoAlamatPesanKostKontrakan().getByStatus(true)!!
            tvNamaAlamatDisimpanKostKontrakan.text = a.name
            tvPhoneAlamatDisimpanKostKontrakan.text = a.phone
            tvTanggalAlamatDisimpanKostKontrakan.text = a.tanggal
            tvAlamatAlamatDisimpanKostKontrakan.text = a.alamat
            btTambahAlamatDisimpanKostKontrakan.text = "Ubah Alamat"

        } else {
            cvAlamatDisimpanKostKontrakan.visibility = View.GONE
            tvKosongDisimpanKostKontrakan.visibility = View.VISIBLE
            btTambahAlamatDisimpanKostKontrakan.text = "Tambah Alamat"
        }
    }

    fun buttonPesan(){
        if (myDb.daoAlamatPesanKostKontrakan().getByStatus(true) != null){
            btBayarDisimpanKostKontrakan.visibility = View.VISIBLE

        } else {
            btBayarDisimpanKostKontrakan.visibility = View.GONE
        }
    }

    private fun mainButton() {
        btTambahAlamatDisimpanKostKontrakan.setOnClickListener {
            startActivity(Intent(this, ListAlamatPesanKostKontrakanActivity::class.java))
        }
        btBayarDisimpanKostKontrakan.setOnClickListener {
            bayar()
        }
    }

    private fun bayar(){
        val user = SharedPref(this).getUser()!!
        val a = myDb.daoAlamatPesanKostKontrakan().getByStatus(true)!!

        val loading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Memuat...").show()

        val listKostKontrakan = myDb.daoSimpanKostKontrakan().getAll() as ArrayList
        val kostkontrakans = ArrayList<CheckoutPesanKostKontrakan.Item>()

        for (b in listKostKontrakan) {
            if (b.selected) {

                val kostkontrakan = CheckoutPesanKostKontrakan.Item()
                kostkontrakan.id = "" + b.id

                kostkontrakans.add(kostkontrakan)
            }
        }

            val pesanKostKontrakan = CheckoutPesanKostKontrakan()
            pesanKostKontrakan.usergeneral_id = "" + user.id
            pesanKostKontrakan.name = a.name
            pesanKostKontrakan.tanggal = a.tanggal
            pesanKostKontrakan.phone = a.phone
            pesanKostKontrakan.detail_lokasi = a.alamat
            pesanKostKontrakan.kostkontrakans = kostkontrakans



        ApiConfig.instanceRetrofit.pesankostkontrakan(pesanKostKontrakan).enqueue(object :Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                loading.dismiss()
                if (!response.isSuccessful){
                    error(response.message())
                    return
                }

                val respon = response.body()!!
                if (respon.success == 1){

                    val jsPesanKostKontrakan = Gson().toJson(pesanKostKontrakan, CheckoutPesanKostKontrakan::class.java)

                    val intent = Intent(this@KirimPemesananKostKontrakanActivity, SuccesKostkontrakanActivity::class.java)
                    intent.putExtra("bokingkostkontrakan", jsPesanKostKontrakan)

                    startActivity(intent)
                    Toast.makeText(this@KirimPemesananKostKontrakanActivity, "Berhasil memesan kost atau kontrakan", Toast.LENGTH_SHORT).show()

                } else {
                    error(respon.message)
                    Toast.makeText(this@KirimPemesananKostKontrakanActivity, "" + respon.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())

            }

        })

    }

    fun error(pesan: String){
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Maaf")
            .setContentText(pesan)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        checkAlamat()
        buttonPesan()
        super.onResume()
    }
}