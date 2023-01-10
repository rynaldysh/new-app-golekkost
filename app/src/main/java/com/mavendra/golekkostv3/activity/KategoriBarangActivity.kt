package com.mavendra.golekkostv3.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.KategoriBarangAdapter
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.Barang
import com.mavendra.golekkostv3.model.ResponModel
import kotlinx.android.synthetic.main.activity_detail_transfer.*
import kotlinx.android.synthetic.main.activity_kategori_barang.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_riwayat_belanja.*
import kotlinx.android.synthetic.main.toolbar_beranda.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import java.util.*
import kotlin.collections.ArrayList


class KategoriBarangActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori_barang)

        Helper().setToolbar(this, toolbarBiasa, "Kategori Barang")

    }

    fun getBarang(){
        ApiConfig.instanceRetrofit.getBarang().enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1){
                    displayBarang(res.barangs)
                }
            }
        })


    }

    fun displayBarang(barangs: ArrayList<Barang>) {

        val layoutManagerBarang = LinearLayoutManager(this)
        layoutManagerBarang.orientation = LinearLayoutManager.VERTICAL

        rvKategoriBarangJualan.adapter = KategoriBarangAdapter(barangs, object : KategoriBarangAdapter.Listeners{
            override fun onClicked(data: Barang) {
                val json = Gson().toJson(data, Barang::class.java)
                val intent = Intent(this@KategoriBarangActivity, DetailBarangActivity::class.java)
                intent.putExtra("kategoribarang", json)
                startActivity(intent)

            }

        })
        rvKategoriBarangJualan.layoutManager = layoutManagerBarang
    }

    override fun onResume() {
        getBarang()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}