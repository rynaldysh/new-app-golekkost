package com.mavendra.golekkostv3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.RiwayatPesanJasaAdapter
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.model.ResponModel
import com.mavendra.golekkostv3.model.PesanJasa
import kotlinx.android.synthetic.main.activity_riwayat_belanja.*
import kotlinx.android.synthetic.main.activity_riwayat_pesan_jasa.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RiwayatPesanJasaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_pesan_jasa)

        Helper().setToolbar(this, toolbarBiasa, "Riwayat Pesan Jasa Angkut")

    }

    fun getRiwayat(){

        val id = SharedPref(this).getUser()!!.id
        ApiConfig.instanceRetrofit.getPesanjasa(id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1){
                    displayRiwayat(res.pesanjasas)
                }
            }
        })
    }

    fun displayRiwayat(pesanjasas: ArrayList<PesanJasa>) {

        val layoutManagerRiwayat = LinearLayoutManager(this)
        layoutManagerRiwayat.orientation = LinearLayoutManager.VERTICAL

        rvRiwayatPesanJasa.adapter = RiwayatPesanJasaAdapter(pesanjasas, object :RiwayatPesanJasaAdapter.Listeners{
            override fun onClicked(data: PesanJasa) {
                val json = Gson().toJson(data, PesanJasa::class.java)
                val intent = Intent(this@RiwayatPesanJasaActivity, DetailPesanJasaActivity::class.java)
                intent.putExtra("transaksipesanjasa", json)
                startActivity(intent)

            }

        })
        rvRiwayatPesanJasa.layoutManager = layoutManagerRiwayat
    }

    override fun onResume() {
        getRiwayat()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}