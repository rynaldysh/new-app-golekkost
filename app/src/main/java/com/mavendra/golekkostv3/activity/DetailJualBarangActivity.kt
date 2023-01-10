package com.mavendra.golekkostv3.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.inyongtisto.myhelper.base.BaseActivity
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.BarangTransaksiAdapter
import com.mavendra.golekkostv3.adapter.RiwayatJualBarangAdapter
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.app.Constants
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.model.*
import kotlinx.android.synthetic.main.activity_detail_transfer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_jasaangkut.*
import kotlinx.android.synthetic.main.activity_detail_jual_barang.*
import kotlinx.android.synthetic.main.activity_detail_transfer.rvBarangDetailTransfer
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_riwayat_jual_barang.*
import kotlinx.android.synthetic.main.toolbar_beranda.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import kotlinx.android.synthetic.main.toolbar_custom_keranjang_detail.*
import kotlinx.android.synthetic.main.toolbar_custom_top_jasa_detail.*


class DetailJualBarangActivity : BaseActivity() {

    var barang = Barang()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_jual_barang)

        val json = intent.getStringExtra("barangpush")
        barang = Gson().fromJson(json, Barang::class.java)

        setData(barang)
        getInfo()
        mainButton()
    }

    fun mainButton(){
        btTerjualDetailJualBarang.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin?")
                .setContentText("Barang anda sudah terjual? tidak dapat di ubah seperti sebelumnya!")
                .setConfirmText("Ya, Habis!")
                .setConfirmClickListener {
                    it.dismissWithAnimation()
                    barangTerjual()
                }
                .setCancelText("Tutup")
                .setCancelClickListener {
                    it.dismissWithAnimation()
                }.show()
        }
    }

    fun barangTerjual(){
        val loading = SweetAlertDialog(this@DetailJualBarangActivity, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Memuat...").show()
        ApiConfig.instanceRetrofit.terjualBarang(barang.id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1){

                    SweetAlertDialog(this@DetailJualBarangActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil")
                        .setContentText("Status barang anda telah berubah menjadi habis")
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                            onBackPressed()
                        }.show()

                    /*
                        Toast.makeText(this@DetailTransferActivity, "Transfer berhasil dibatalkan", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                        *//*displayRiwayat(res.transaksis)*/
                } else {
                    error(res.message)
                }
            }
        })
    }

    fun error(pesan: String){
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Maaf")
            .setContentText(pesan)
            .show()
    }

    @SuppressLint("SetTextI18n")
    fun setData(barang: Barang) {

        val newFormat = "dd MMMM yyyy, kk:mm:ss"
        tvTanggalDetailJualBarang.text = Helper().convertDate(barang.created_att, newFormat)
        /*tvTanggalDetailTransfer.text = transaksi.created_att*/

        tvStatusDetailJualBarang.text = barang.status


        if(barang.status != "TERSEDIA") llBawahJualBarang.visibility = View.GONE

        var color = getColor(R.color.terjual)
        if (barang.status == "TERSEDIA") color = getColor(R.color.tersedia)
        else if (barang.status == "HABIS") color = getColor(R.color.terjual)

        tvStatusDetailJualBarang.setTextColor(color)
    }

    private fun getInfo() {

        //set Value
        tvNamaPemilikJualBarang.text = barang.name_pemilik
        tvNamaJualBarang.text = barang.name
        tvHargaJualBarang.text = Helper().gantiRupiah(barang.harga)
        tvKodeInputJualBarang.text = barang.kode_input_barang
        tvLokasiJualBarang.text = barang.lokasi
        tvDeskripsiJualBarang.text = barang.deskripsi

        val image = Constants.barang_url + barang.image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.beranda_ex_kostt)
            .error(R.drawable.beranda_ex_kostt)
            .into(ivgambarJualBarang)

        Helper().setToolbar(this, toolbarBiasa, barang.name)

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }



}