package com.mavendra.golekkostv3.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.drjacky.imagepicker.ImagePicker
import com.google.gson.Gson
import com.inyongtisto.myhelper.base.BaseActivity
import com.inyongtisto.myhelper.extension.showErrorDialog
import com.inyongtisto.myhelper.extension.showSuccessDialog
import com.inyongtisto.myhelper.extension.toMultipartBody
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.BarangTransaksiAdapter
import com.mavendra.golekkostv3.adapter.BarangTransaksiPenjualanAdapter
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.DetailTransaksi
import com.mavendra.golekkostv3.model.ResponModel
import com.mavendra.golekkostv3.model.Transaksi
import kotlinx.android.synthetic.main.activity_detail_transfer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_transaksi_penjualan.*
import kotlinx.android.synthetic.main.activity_detail_transfer.llBawah
import kotlinx.android.synthetic.main.activity_detail_transfer.tvAlamatDetailTransfer
import kotlinx.android.synthetic.main.activity_detail_transfer.tvBiayaKirimDetailTransfer
import kotlinx.android.synthetic.main.activity_detail_transfer.tvKodeUnikDetailTransfer
import kotlinx.android.synthetic.main.activity_detail_transfer.tvPenerimaDetailTransfer
import kotlinx.android.synthetic.main.activity_detail_transfer.tvTotalBelanjaDetailTransfer
import kotlinx.android.synthetic.main.activity_detail_transfer.tvTotalDetailTransfer
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar_beranda.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import java.io.File


class DetailTransaksiPenjualanActivity : BaseActivity() {

    var transaksi = Transaksi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaksi_penjualan)

        Helper().setToolbar(this, toolbarBiasa, "Detail Transaksi Penjualan")

        val json = intent.getStringExtra("transaksipenjualan")
        transaksi = Gson().fromJson(json, Transaksi::class.java)

        setData(transaksi)
        displayDetailTransfer(transaksi.details)
        mainButton()
    }

    fun mainButton(){
        btProsesDetailTransaksiPenjualan.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin")
                .setContentText("Mengubah status barang menjadi proses?")
                .setConfirmText("Ya, batalkan!")
                .setConfirmClickListener {
                        it.dismissWithAnimation()
                        prosesBarang()
                }
                .setCancelText("Tutup")
                .setCancelClickListener {
                    it.dismissWithAnimation()
                }.show()
        }

        btDikirimDetailTransaksiPenjualan.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin")
                .setContentText("Mengubah status barang menjadi dikirim?")
                .setConfirmText("Ya, batalkan!")
                .setConfirmClickListener {
                    it.dismissWithAnimation()
                    dikirimBarang()
                }
                .setCancelText("Tutup")
                .setCancelClickListener {
                    it.dismissWithAnimation()
                }.show()
        }
    }



    fun prosesBarang(){
        val loading = SweetAlertDialog(this@DetailTransaksiPenjualanActivity, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Memuat...").show()
        ApiConfig.instanceRetrofit.prosesBarang(transaksi.id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1){

                    SweetAlertDialog(this@DetailTransaksiPenjualanActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil")
                        .setContentText("Status berhasil diubah menjadi proses")
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

    fun dikirimBarang(){
        val loading = SweetAlertDialog(this@DetailTransaksiPenjualanActivity, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Memuat...").show()
        ApiConfig.instanceRetrofit.dikirimBarang(transaksi.id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1){

                    SweetAlertDialog(this@DetailTransaksiPenjualanActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil")
                        .setContentText("Status berhasil diubah menjadi dikirim")
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
    fun setData(transaksi: Transaksi) {

        val newFormat = "dd MMMM yyyy, kk:mm:ss"
        tvTanggalDetailTransaksiPenjualan.text = Helper().convertDate(transaksi.created_att, newFormat)
        /*tvTanggalDetailTransfer.text = transaksi.created_att*/

        tvStatusDetailTransaksiPenjualan.text = transaksi.status
        tvPenerimaDetailTransaksiPenjualan.text = transaksi.name + " - " + transaksi.phone
        tvAlamatDetailTransaksiPenjualan.text = transaksi.detail_lokasi
        tvTotalBelanjaDetailTransaksiPenjualan.text = Helper().gantiRupiah(transaksi.total_harga)
        tvBiayaKirimDetailTransaksiPenjualan.text = Helper().gantiRupiah(transaksi.ongkir)
        tvTotalDetailTransaksiPenjualan.text = Helper().gantiRupiah(transaksi.total_transfer)

        var color = getColor(R.color.menunggu)
        if (transaksi.status == "SELESAI") color = getColor(R.color.selesai)
        else if (transaksi.status == "BATAL") color = getColor(R.color.batal)

        tvStatusDetailTransaksiPenjualan.setTextColor(color)

        if(transaksi.status != "DIBAYAR"){
            btProsesDetailTransaksiPenjualan.visibility = View.GONE
        } else {
            btProsesDetailTransaksiPenjualan.visibility = View.VISIBLE
        }

        if (transaksi.status == "PROSES"){
            btDikirimDetailTransaksiPenjualan.visibility = View.VISIBLE
        } else {
            btDikirimDetailTransaksiPenjualan.visibility = View.GONE
        }
    }

    fun displayDetailTransfer(transaksis: ArrayList<DetailTransaksi>) {

        val layoutManagerRiwayat = LinearLayoutManager(this)
        layoutManagerRiwayat.orientation = LinearLayoutManager.VERTICAL

        rvBarangDetailTransaksiPenjualan.adapter = BarangTransaksiPenjualanAdapter(transaksis)
        rvBarangDetailTransaksiPenjualan.layoutManager = layoutManagerRiwayat

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}