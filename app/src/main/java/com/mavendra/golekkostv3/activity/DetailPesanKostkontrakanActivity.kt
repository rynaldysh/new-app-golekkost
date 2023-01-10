package com.mavendra.golekkostv3.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.KostkontrakanPesanAdapter
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.*
import kotlinx.android.synthetic.main.activity_detail_transfer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_detail_pesan_kostkontrakan.*
import kotlinx.android.synthetic.main.toolbar_beranda.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import kotlinx.android.synthetic.main.toolbar_custom_bottom_detail_kostkontrakan_pesan.*


class DetailPesanKostkontrakanActivity : AppCompatActivity() {

    var pesankostkontrakan = PesanKostkontrakan()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pesan_kostkontrakan)

        Helper().setToolbar(this, toolbarBiasa, "Detail Pemesanan Kost atau Kontrakan")

        val json = intent.getStringExtra("transaksipesankostkontrakan")
        pesankostkontrakan = Gson().fromJson(json, PesanKostkontrakan::class.java)

        setData(pesankostkontrakan)
        displayDetailTransfer(pesankostkontrakan.details)
        mainButton()
    }

    fun mainButton(){
        btBatalDetailPesanKostkontrakan.setOnClickListener {
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

        btSelesaiDetailPesanKostkontrakan.setOnClickListener {
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
        val loading = SweetAlertDialog(this@DetailPesanKostkontrakanActivity, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Memuat...").show()
        ApiConfig.instanceRetrofit.batalPesankostkontrakan(pesankostkontrakan.id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1){

                    SweetAlertDialog(this@DetailPesanKostkontrakanActivity, SweetAlertDialog.SUCCESS_TYPE)
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
        val loading = SweetAlertDialog(this@DetailPesanKostkontrakanActivity, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Memuat...").show()
        ApiConfig.instanceRetrofit.selesaiPesankostkontrakan(pesankostkontrakan.id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1){

                    SweetAlertDialog(this@DetailPesanKostkontrakanActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil")
                        .setContentText("Pemesanan berhasil diselesaikan")
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
    fun setData(pesankostkontrakan: PesanKostkontrakan) {

        val newFormat = "dd MMMM yyyy, kk:mm:ss"
        tvTanggalDetailPesanKostkontrakan.text = Helper().convertDate(pesankostkontrakan.created_att, newFormat)
        /*tvTanggalDetailTransfer.text = transaksi.created_att*/

        tvStatusDetailPesanKostkontrakan.text = pesankostkontrakan.status
        tvPemesanPesanKostkontrakan.text = pesankostkontrakan.name + " - " + pesankostkontrakan.phone
        tvAlamatPemesanDetailPesanKostkontrakan.text = pesankostkontrakan.detail_lokasi
        tvKodePemesanDetailPesanKostkontrakan.text = pesankostkontrakan.kode_pesan_kostkontrakan

        if(pesankostkontrakan.status != "MENUNGGU") llBawahPesanKostkontrakan.visibility = View.GONE

        var color = getColor(R.color.menunggu)
        if (pesankostkontrakan.status == "SELESAI") color = getColor(R.color.selesai)
        else if (pesankostkontrakan.status == "BATAL") color = getColor(R.color.batal)

        tvStatusDetailPesanKostkontrakan.setTextColor(color)
    }

    fun displayDetailTransfer(pesankostkontrakans: ArrayList<DetailPesanKostkontrakan>) {

        val layoutManagerRiwayat = LinearLayoutManager(this)
        layoutManagerRiwayat.orientation = LinearLayoutManager.VERTICAL

        rvDetailPesanKostkontrakan.adapter = KostkontrakanPesanAdapter(pesankostkontrakans)
        rvDetailPesanKostkontrakan.layoutManager = layoutManagerRiwayat


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}