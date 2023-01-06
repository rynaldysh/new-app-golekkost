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

    override fun onResume() {
        getKostkontrakan()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}