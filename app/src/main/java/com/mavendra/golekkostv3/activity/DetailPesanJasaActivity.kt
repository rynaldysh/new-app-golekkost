package com.mavendra.golekkostv3.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.JasaangkutPesanAdapter
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.*
import kotlinx.android.synthetic.main.activity_detail_transfer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_detail_pesan_jasa.*
import kotlinx.android.synthetic.main.activity_detail_pesan_kostkontrakan.*
import kotlinx.android.synthetic.main.toolbar_beranda.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import kotlinx.android.synthetic.main.toolbar_custom_bottom_detail_jasa_pesan.*


class DetailPesanJasaActivity : AppCompatActivity() {

    var pesanjasa = PesanJasa()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pesan_jasa)

        Helper().setToolbar(this, toolbarBiasa, "Detail Pemesanan Jasa Angkut")

        val json = intent.getStringExtra("transaksipesanjasa")
        pesanjasa = Gson().fromJson(json, PesanJasa::class.java)

        setData(pesanjasa)
        displayDetailTransfer(pesanjasa.details)
        mainButton()
    }

    fun mainButton(){
        btBatalDetailPesanJasa.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin?")
                .setContentText("Pesanan akan dibatalkan!")
                .setConfirmText("Ya, batalkan!")
                .setConfirmClickListener {
                        it.dismissWithAnimation()
                        batalCheckout()
                }
                .setCancelText("Tutup")
                .setCancelClickListener {
                    it.dismissWithAnimation()
                }.show()
        }

        btSelesaiDetailPesanJasa.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin?")
                .setContentText("Sudah diselesaikan!")
                .setConfirmText("Ya, selesai!")
                .setConfirmClickListener {
                    it.dismissWithAnimation()
                    selesaiCheckout()
                }
                .setCancelText("Tutup")
                .setCancelClickListener {
                    it.dismissWithAnimation()
                }.show()
        }
    }

    fun batalCheckout(){
        val loading = SweetAlertDialog(this@DetailPesanJasaActivity, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Memuat...").show()
        ApiConfig.instanceRetrofit.batalPesanJasa(pesanjasa.id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1){

                    SweetAlertDialog(this@DetailPesanJasaActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil")
                        .setContentText("Pemesanan berhasil dibatalkan")
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

    fun selesaiCheckout(){
        val loading = SweetAlertDialog(this@DetailPesanJasaActivity, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Memuat...").show()
        ApiConfig.instanceRetrofit.selesaiPesanJasa(pesanjasa.id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1){

                    SweetAlertDialog(this@DetailPesanJasaActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil")
                        .setContentText("Pemesanan sudah diselesaikan")
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
    fun setData(pesanjasa: PesanJasa) {

        val newFormat = "dd MMMM yyyy, kk:mm:ss"
        tvTanggalDetailPesanJasa.text = Helper().convertDate(pesanjasa.created_att, newFormat)
        /*tvTanggalDetailTransfer.text = transaksi.created_att*/

        tvStatusDetailPesanJasa.text = pesanjasa.status
        tvPemesanPesanJasa.text = pesanjasa.name + " - " + pesanjasa.phone
        tvAlamatAsalPemesanDetailPesanJasa.text = pesanjasa.detail_lokasi_asal + "(" + pesanjasa.type_asal + ")"
        tvAlamatTujuanPemesanDetailPesanJasa.text = pesanjasa.detail_lokasi_tujuan + "(" + pesanjasa.type_tujuan + ")"
        tvKodePemesanDetailPesanJasa.text = pesanjasa.kode_pesan_jasa

        if(pesanjasa.status != "MENUNGGU") llBawahPesanJasa.visibility = View.GONE

        var color = getColor(R.color.menunggu)
        if (pesanjasa.status == "SELESAI") color = getColor(R.color.selesai)
        else if (pesanjasa.status == "BATAL") color = getColor(R.color.batal)

        tvStatusDetailPesanJasa.setTextColor(color)
    }

    fun displayDetailTransfer(pesanjasas: ArrayList<DetailPesanJasa>) {

        val layoutManagerRiwayat = LinearLayoutManager(this)
        layoutManagerRiwayat.orientation = LinearLayoutManager.VERTICAL

        rvDetailPesanJasa.adapter = JasaangkutPesanAdapter(pesanjasas)
        rvDetailPesanJasa.layoutManager = layoutManagerRiwayat


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}