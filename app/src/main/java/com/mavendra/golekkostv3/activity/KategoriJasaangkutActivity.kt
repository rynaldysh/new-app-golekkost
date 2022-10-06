package com.mavendra.golekkostv3.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.KategoriBarangAdapter
import com.mavendra.golekkostv3.adapter.KategoriJasaangkutAdapter
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.Barang
import com.mavendra.golekkostv3.model.Jasaangkut
import com.mavendra.golekkostv3.model.ResponModel
import kotlinx.android.synthetic.main.activity_detail_transfer.*
import kotlinx.android.synthetic.main.activity_kategori_barang.*
import kotlinx.android.synthetic.main.activity_kategori_barang.rvKategoriBarangJualan
import kotlinx.android.synthetic.main.activity_kategori_jasaangkut.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_riwayat_belanja.*
import kotlinx.android.synthetic.main.toolbar_beranda.*
import kotlinx.android.synthetic.main.toolbar_biasa.*


class KategoriJasaangkutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori_jasaangkut)

        Helper().setToolbar(this, toolbarBiasa, "Kategori Jasa Angkut")

    }

    fun getJasaAngkut(){
        ApiConfig.instanceRetrofit.getJasaAngkut().enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1){
                    displayJasaangkut(res.jasaangkuts)
                }
            }
        })
    }

    fun displayJasaangkut(jasaangkuts: ArrayList<Jasaangkut>) {

        val layoutManagerJasaangkut = LinearLayoutManager(this)
        layoutManagerJasaangkut.orientation = LinearLayoutManager.VERTICAL

        rvKategoriJasaangkut.adapter = KategoriJasaangkutAdapter(jasaangkuts, object : KategoriJasaangkutAdapter.Listeners{
            override fun onClicked(data: Jasaangkut) {
                val json = Gson().toJson(data, Jasaangkut::class.java)
                val intent = Intent(this@KategoriJasaangkutActivity, DetailJasaAngkutActivity::class.java)
                intent.putExtra("kategorijasaangkut", json)
                startActivity(intent)

            }

        })
        rvKategoriJasaangkut.layoutManager = layoutManagerJasaangkut
    }

    override fun onResume() {
        getJasaAngkut()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}