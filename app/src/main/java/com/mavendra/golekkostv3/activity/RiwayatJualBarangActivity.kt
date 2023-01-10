package com.mavendra.golekkostv3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.RiwayatJualBarangAdapter
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.model.Barang
import com.mavendra.golekkostv3.model.ResponModel
import kotlinx.android.synthetic.main.activity_riwayat_belanja.*
import kotlinx.android.synthetic.main.activity_riwayat_jual_barang.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import kotlinx.android.synthetic.main.toolbar_custom_bottom_jual_barang.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RiwayatJualBarangActivity : AppCompatActivity() {

    var barang = Barang()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_jual_barang)

        Helper().setToolbar(this, toolbarBiasa, "Riwayat Barang Anda")

        mainButton()

    }

    private fun mainButton() {
        idTambahJualBarang.setOnClickListener {
            startActivity(Intent(this, JualBarangActivity::class.java))
        }
    }

    fun getRiwayat(){
        val id = SharedPref(this).getUser()!!.id
        ApiConfig.instanceRetrofit.getCreateBarang(id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1){
                    displayRiwayat(res.barangs)
                }
            }
        })
    }

    fun check(){
        if (barang.toString().isNotEmpty()){
            tvKosongRiwayatJualBarang.visibility = View.GONE
            rvRiwayatJualBarang.visibility = View.VISIBLE
        } else {
            tvKosongRiwayatJualBarang.visibility = View.VISIBLE
            rvRiwayatJualBarang.visibility = View.GONE
        }
    }

    fun displayRiwayat(barangs: ArrayList<Barang>) {

        val layoutManagerRiwayat = LinearLayoutManager(this)
        layoutManagerRiwayat.orientation = LinearLayoutManager.VERTICAL

        rvRiwayatJualBarang.adapter = RiwayatJualBarangAdapter(barangs, object :RiwayatJualBarangAdapter.Listeners{
            override fun onClicked(data: Barang) {
                val json = Gson().toJson(data, Barang::class.java)
                val intent = Intent(this@RiwayatJualBarangActivity, DetailJualBarangActivity::class.java)
                intent.putExtra("barangpush", json)
                startActivity(intent)

            }

        })
        rvRiwayatJualBarang.layoutManager = layoutManagerRiwayat
    }

    override fun onResume() {
        check()
        getRiwayat()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}