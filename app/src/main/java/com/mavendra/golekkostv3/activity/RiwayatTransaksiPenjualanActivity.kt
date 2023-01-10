package com.mavendra.golekkostv3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.RiwayatAdapter
import com.mavendra.golekkostv3.adapter.RiwayatTransaksiPenjualanAdapter
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.ResponModel
import com.mavendra.golekkostv3.model.Transaksi
import kotlinx.android.synthetic.main.activity_riwayat_belanja.*
import kotlinx.android.synthetic.main.activity_riwayat_belanja.rvRiwayat
import kotlinx.android.synthetic.main.activity_riwayat_transaksi_penjualan.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RiwayatTransaksiPenjualanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_transaksi_penjualan)

        Helper().setToolbar(this, toolbarBiasa, "Riwayat Transaksi Penjualan")

    }

    fun getRiwayat(){
        ApiConfig.instanceRetrofit.getRiwayatPenjualan().enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1){
                    displayRiwayat(res.transaksis)
                }
            }
        })
    }

    fun displayRiwayat(transaksis: ArrayList<Transaksi>) {

        val layoutManagerRiwayat = LinearLayoutManager(this)
        layoutManagerRiwayat.orientation = LinearLayoutManager.VERTICAL

        rvRiwayatTransaksiPenjualan.adapter = RiwayatTransaksiPenjualanAdapter(transaksis, object : RiwayatTransaksiPenjualanAdapter.Listeners{
            override fun onClicked(data: Transaksi) {
                val json = Gson().toJson(data, Transaksi::class.java)
                val intent = Intent(this@RiwayatTransaksiPenjualanActivity, DetailTransaksiPenjualanActivity::class.java)
                intent.putExtra("transaksipenjualan", json)
                startActivity(intent)

            }

        })
        rvRiwayatTransaksiPenjualan.layoutManager = layoutManagerRiwayat
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