package com.mavendra.golekkostv3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.RiwayatPesanKostkontrakanAdapter
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.model.ResponModel
import com.mavendra.golekkostv3.model.PesanKostkontrakan
import kotlinx.android.synthetic.main.activity_riwayat_belanja.*
import kotlinx.android.synthetic.main.activity_riwayat_pesan_jasa.*
import kotlinx.android.synthetic.main.activity_riwayat_pesan_kostkontrakan.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RiwayatPesanKostkontrakanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_pesan_kostkontrakan)

        Helper().setToolbar(this, toolbarBiasa, "Riwayat Pesan Kost atau Kontrakan")

    }

    fun getRiwayat(){

        val id = SharedPref(this).getUser()!!.id
        ApiConfig.instanceRetrofit.getPesanKostkontrakan(id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1){
                    displayRiwayat(res.pesankostkontrakans)
                }
            }
        })
    }

    fun displayRiwayat(pesankostkontrakans: ArrayList<PesanKostkontrakan>) {

        val layoutManagerRiwayat = LinearLayoutManager(this)
        layoutManagerRiwayat.orientation = LinearLayoutManager.VERTICAL

        rvRiwayatPesanKostkontrakan.adapter = RiwayatPesanKostkontrakanAdapter(pesankostkontrakans, object :RiwayatPesanKostkontrakanAdapter.Listeners{
            override fun onClicked(data: PesanKostkontrakan) {
                val json = Gson().toJson(data, PesanKostkontrakan::class.java)
                val intent = Intent(this@RiwayatPesanKostkontrakanActivity, DetailPesanKostkontrakanActivity::class.java)
                intent.putExtra("transaksipesankostkontrakan", json)
                startActivity(intent)

            }

        })
        rvRiwayatPesanKostkontrakan.layoutManager = layoutManagerRiwayat
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