package com.mavendra.golekkostv3.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.inyongtisto.myhelper.base.BaseActivity
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.model.*
import kotlinx.android.synthetic.main.activity_detail_transfer.*
import kotlinx.android.synthetic.main.activity_jual_barang.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JualBarangActivity : BaseActivity() {

    val barang = Barang()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jual_barang)

        Helper().setToolbar(this, toolbarBiasa, "Input Daftar Barang Jual")

        mainButton()

    }

    fun mainButton(){
        btUploadDataBarang.setOnClickListener {
            uploadData()
        }
    }

    fun uploadData(){
        val user = SharedPref(this).getUser()!!
        val name_pemilik = etNamaPemilikBuatBarang.text.toString().trim()
        val notelfon = etNotelfonBuatBarang.text.toString().trim()
        val name = etNamaBarang.text.toString().trim()
        val harga = etHargaBarang.text.toString().trim()
        val lokasi = etAlamatAsalPesanJasa.text.toString().trim()
        val deskripsi = etDeskripsiBarang.text.toString().trim()

        if (name_pemilik.isEmpty()){
            etNamaPemilikBuatBarang.error = "Kolom nama barang tidak boleh kosong"
            etNamaPemilikBuatBarang.requestFocus()
            return
        } else if (notelfon.isEmpty()){
            etNotelfonBuatBarang.error = "Kolom nama barang tidak boleh kosong"
            etNotelfonBuatBarang.requestFocus()
            return
        } else if (name.isEmpty()){
            etNamaBarang.error = "Kolom nama barang tidak boleh kosong"
            etNamaBarang.requestFocus()
            return
        } else if (harga.isEmpty()){
            etHargaBarang.error = "Kolom harga barang tidak boleh kosong"
            etHargaBarang.requestFocus()
            return
        } else if (lokasi.isEmpty()){
            etAlamatAsalPesanJasa.error = "Kolom alamat tidak boleh kosong"
            etAlamatAsalPesanJasa.requestFocus()
            return
        } else if (deskripsi.isEmpty()){
            etDeskripsiBarang.error = "Kolom deskripsi barang tidak boleh kosong"
            etDeskripsiBarang.requestFocus()
            return
        }

        ApiConfig.instanceRetrofit.uploadbarang(usergeneral_id = user.id, name_pemilik, notelfon, name, harga, lokasi, deskripsi).enqueue(object :
            Callback<ResponModel> {

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                //pb.visibility = View.GONE
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                if (!response.isSuccessful){
                    error(response.message())
                    return
                }

                val respon = response.body()!!
                if (respon.success == 1){

                    val jsBarang= Gson().toJson(respon.barang, Barang::class.java)

                    val intent = Intent(this@JualBarangActivity, UploadFotoBarangActivity::class.java)
                    intent.putExtra("barangpush", jsBarang)

                    startActivity(intent)
                    Toast.makeText(this@JualBarangActivity, "Berhasil memesan jasa ", Toast.LENGTH_SHORT).show()
                    onBackPressed()


                } else {
                    error(respon.message)
                }

            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
