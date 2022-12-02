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
import kotlinx.android.synthetic.main.activity_pengiriman.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class KirimPemesananJasaActivity : AppCompatActivity() {

    lateinit var myDb: MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kirim_pemesanan_jasa)
        myDb = MyDatabase.getInstance(this)!!
        Helper().setToolbar(this, toolbarBiasa, "Pemesanan Jasa Angkut")

        mainButton()

    }

    @SuppressLint("SetTextI18n")
    fun checkAlamat(){
        if (myDb.daoAlamatPesanJasa().getByStatus(true) != null){
            cvAlamatDisimpanJasa.visibility = View.VISIBLE
            tvKosongDisimpanJasa.visibility = View.GONE

            val a = myDb.daoAlamatPesanJasa().getByStatus(true)!!
            tvNamaAlamatDisimpanJasa.text = a.name
            tvPhoneAlamatDisimpanJasa.text = a.phone
            tvAlamatAlamatAsalDisimpanJasa.text = a.detail_lokasi_asal
            btTambahAlamatDisimpanJasa.text = "Ubah Alamat"

        } else {
            cvAlamatDisimpanJasa.visibility = View.GONE
            tvKosongDisimpanJasa.visibility = View.VISIBLE
            btTambahAlamatDisimpanJasa.text = "Tambah Alamat"
        }
    }

    fun buttonPesan(){
        if (myDb.daoAlamatPesanJasa().getByStatus(true) != null){
            btBayarDisimpanJasa.visibility = View.VISIBLE

        } else {
            btBayarDisimpanJasa.visibility = View.GONE
        }
    }

    private fun mainButton() {
        btTambahAlamatDisimpanJasa.setOnClickListener {
            startActivity(Intent(this, ListAlamatPesanJasaActivity::class.java))
        }
        btBayarDisimpanJasa.setOnClickListener {
            pesan()
        }
    }

    private fun pesan(){
        val user = SharedPref(this).getUser()!!
        val a = myDb.daoAlamatPesanJasa().getByStatus(true)!!

        val loading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Memuat...").show()

        val listJasaangkut = myDb.daoSimpanJasa().getAll() as ArrayList
        val jasaangkuts = ArrayList<CheckoutPesanJasa.Item>()

        for (b in listJasaangkut) {
            if (b.selected) {

                val jasaangkut = CheckoutPesanJasa.Item()
                jasaangkut.id = "" + b.id

                jasaangkuts.add(jasaangkut)
            }
        }

            val pesanJasa = CheckoutPesanJasa()
            pesanJasa.usergeneral_id = "" + user.id
            pesanJasa.name = a.name
            pesanJasa.tanggal = a.tanggal
            pesanJasa.phone = a.phone
            pesanJasa.detail_lokasi_asal = a.detail_lokasi_asal
            pesanJasa.detail_lokasi_tujuan = a.detail_lokasi_tujuan
            pesanJasa.type_asal = a.type_asal
            pesanJasa.type_tujuan = a.type_tujuan
            pesanJasa.jasaangkuts = jasaangkuts



        ApiConfig.instanceRetrofit.pesanjasa(pesanJasa).enqueue(object :Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                loading.dismiss()
                if (!response.isSuccessful){
                    error(response.message())
                    return
                }

                val respon = response.body()!!
                if (respon.success == 1){

                    val jsPesanJasa = Gson().toJson(pesanJasa, CheckoutPesanJasa::class.java)

                    val intent = Intent(this@KirimPemesananJasaActivity, SuccesJasaActivity::class.java)
                    intent.putExtra("bokingjasa", jsPesanJasa)

                    startActivity(intent)
                    Toast.makeText(this@KirimPemesananJasaActivity, "Berhasil memesan jasa ", Toast.LENGTH_SHORT).show()

                } else {
                    error(respon.message)
                    Toast.makeText(this@KirimPemesananJasaActivity, "" + respon.message, Toast.LENGTH_SHORT).show()
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