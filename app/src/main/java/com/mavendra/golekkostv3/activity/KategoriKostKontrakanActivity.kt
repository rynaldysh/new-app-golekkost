package com.mavendra.golekkostv3.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.KategoriBarangAdapter
import com.mavendra.golekkostv3.adapter.KategoriJasaangkutAdapter
import com.mavendra.golekkostv3.adapter.KategoriKostkontrakanAdapter
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.Barang
import com.mavendra.golekkostv3.model.Jasaangkut
import com.mavendra.golekkostv3.model.Kostkontrakan
import com.mavendra.golekkostv3.model.ResponModel
import kotlinx.android.synthetic.main.activity_detail_transfer.*
import kotlinx.android.synthetic.main.activity_kategori_barang.*
import kotlinx.android.synthetic.main.activity_kategori_barang.rvKategoriBarangJualan
import kotlinx.android.synthetic.main.activity_kategori_jasaangkut.*
import kotlinx.android.synthetic.main.activity_kategori_kost_kontrakan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_riwayat_belanja.*
import kotlinx.android.synthetic.main.toolbar_beranda.*
import kotlinx.android.synthetic.main.toolbar_biasa.*


class KategoriKostKontrakanActivity : AppCompatActivity() {

    val kostkontrakan = Kostkontrakan()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori_kost_kontrakan)

        Helper().setToolbar(this, toolbarBiasa, "Kategori Kost atau Kontrakan")

    }

    fun getKostkontrakan(){
        ApiConfig.instanceRetrofit.getKostKontrakan().enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1){
                    displayKostkontrakan(res.kostkontrakans)
                }
            }
        })
    }

    fun displayKostkontrakan(kostkontrakans: ArrayList<Kostkontrakan>) {

        val layoutManagerKostkontrakan = LinearLayoutManager(this)
        layoutManagerKostkontrakan.orientation = LinearLayoutManager.VERTICAL

        rvKategoriKostkontrakan.adapter = KategoriKostkontrakanAdapter(kostkontrakans, object : KategoriKostkontrakanAdapter.Listeners{
            override fun onClicked(data: Kostkontrakan) {
                val json = Gson().toJson(data, Kostkontrakan::class.java)
                val intent = Intent(this@KategoriKostKontrakanActivity, DetailKostKontrakanActivity::class.java)
                intent.putExtra("kategorikostkontrakan", json)
                startActivity(intent)

            }

        })
        rvKategoriKostkontrakan.layoutManager = layoutManagerKostkontrakan
    }

    fun filterKostKontrakan(){
        btSlemanBulan.setOnClickListener {
            ApiConfig.instanceRetrofit.getKostKontrakanFiltSlemanBulan().enqueue(object :
                Callback<ResponModel> {
                override fun onFailure(call: Call<ResponModel>, t: Throwable) {

                }

                override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                    val res = response.body()!!
                    if (res.success == 1){
                        displayKostkontrakan(res.kostkontrakans)
                    }
                }
            })
        }

        btSlemanTahun.setOnClickListener {
            ApiConfig.instanceRetrofit.getKostKontrakanFiltSlemanTahun().enqueue(object :
                Callback<ResponModel> {
                override fun onFailure(call: Call<ResponModel>, t: Throwable) {

                }

                override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                    val res = response.body()!!
                    if (res.success == 1){
                        displayKostkontrakan(res.kostkontrakans)
                    }
                }
            })
        }

        btBantulBulan.setOnClickListener {
            ApiConfig.instanceRetrofit.getKostKontrakanFiltSlemanBulan().enqueue(object :
                Callback<ResponModel> {
                override fun onFailure(call: Call<ResponModel>, t: Throwable) {

                }

                override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                    val res = response.body()!!
                    if (res.success == 1){
                        displayKostkontrakan(res.kostkontrakans)
                    }
                }
            })
        }

        btBantulTahun.setOnClickListener {
            ApiConfig.instanceRetrofit.getKostKontrakanFiltBantulTahun().enqueue(object :
                Callback<ResponModel> {
                override fun onFailure(call: Call<ResponModel>, t: Throwable) {

                }

                override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                    val res = response.body()!!
                    if (res.success == 1){
                        displayKostkontrakan(res.kostkontrakans)
                    }
                }
            })
        }

        btGunungKidulBulan.setOnClickListener {
            ApiConfig.instanceRetrofit.getKostKontrakanFiltGunungKidulBulan().enqueue(object :
                Callback<ResponModel> {
                override fun onFailure(call: Call<ResponModel>, t: Throwable) {

                }

                override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                    val res = response.body()!!
                    if (res.success == 1){
                        displayKostkontrakan(res.kostkontrakans)
                    }
                }
            })
        }

        btGunungKidulTahun.setOnClickListener {
            ApiConfig.instanceRetrofit.getKostKontrakanFiltGunungKidulTahun().enqueue(object :
                Callback<ResponModel> {
                override fun onFailure(call: Call<ResponModel>, t: Throwable) {

                }

                override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                    val res = response.body()!!
                    if (res.success == 1){
                        displayKostkontrakan(res.kostkontrakans)
                    }
                }
            })
        }

        btKotaYogyakartaBulan.setOnClickListener {
            ApiConfig.instanceRetrofit.getKostKontrakanFiltKotaYoogyakartaBulan().enqueue(object :
                Callback<ResponModel> {
                override fun onFailure(call: Call<ResponModel>, t: Throwable) {

                }

                override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                    val res = response.body()!!
                    if (res.success == 1){
                        displayKostkontrakan(res.kostkontrakans)
                    }
                }
            })
        }

        btKotaYogyakartaTahun.setOnClickListener {
            ApiConfig.instanceRetrofit.getKostKontrakanFiltKotaYoogyakartaTahun().enqueue(object :
                Callback<ResponModel> {
                override fun onFailure(call: Call<ResponModel>, t: Throwable) {

                }

                override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                    val res = response.body()!!
                    if (res.success == 1){
                        displayKostkontrakan(res.kostkontrakans)
                    }
                }
            })
        }
    }

    override fun onResume() {
        getKostkontrakan()
        filterKostKontrakan()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}