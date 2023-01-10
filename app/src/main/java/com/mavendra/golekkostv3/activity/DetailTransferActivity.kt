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
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar_beranda.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import java.io.File


class DetailTransferActivity : BaseActivity() {

    var transaksi = Transaksi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transfer)

        Helper().setToolbar(this, toolbarBiasa, "Detail Belanja Barang")

        val json = intent.getStringExtra("transaksi")
        transaksi = Gson().fromJson(json, Transaksi::class.java)

        /*checkProses()*/
        setData(transaksi)
        displayDetailTransfer(transaksi.details)
        mainButton()
    }

    fun mainButton(){
        btBatalDetailTransfer.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin?")
                .setContentText("Transaksi akan dibatalkan dan tidak bisa dikembalikan!")
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

        btBayarDetailTransfer.setOnClickListener {
            imagePick()
        }

        btDiterimaDetailTransfer.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin")
                .setContentText("Barang yang anda beli sudah diterima?")
                .setConfirmText("Ya, batalkan!")
                .setConfirmClickListener {
                    it.dismissWithAnimation()
                    diterimaCheckout()
                }
                .setCancelText("Tutup")
                .setCancelClickListener {
                    it.dismissWithAnimation()
                }.show()
        }
    }

    private var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val uri = it.data?.data!!
            Log.d("TAG", "URI IMAGE: "+uri)
            val fileUri: Uri = uri
            dialodUpload(File(fileUri.path))
        }
    }

    var alertDialog : AlertDialog? = null

    @SuppressLint("inflateParams")
    private fun dialodUpload(file: File){

        val view = layoutInflater
        val layout = view.inflate(R.layout.view_upload_bukti, null)

        val imageView : ImageView = layout.findViewById(R.id.image)
        val btUplaod : Button = layout.findViewById(R.id.btUpload)
        val btTake : Button = layout.findViewById(R.id.btAmbilGambar)

        Picasso.get()
            .load(file)
            .into(imageView)

        btUplaod.setOnClickListener {
            upload(file)
        }

        btTake.setOnClickListener {
            imagePick()
        }

        alertDialog = AlertDialog.Builder(this).create()
        alertDialog!!.setView(layout)
        alertDialog!!.setCancelable(true)
        alertDialog!!.show()

    }

    private fun imagePick(){
        ImagePicker.with(this)
            .crop()
            .maxResultSize(512,512)
            .createIntentFromDialog { launcher.launch(it) }
    }

    private fun upload(file: File){

        val fileImage = file.toMultipartBody()

        progress.show()
        ApiConfig.instanceRetrofit.uploadBuktiTransfer(transaksi.id, fileImage!!).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                progress.dismiss()
                showErrorDialog(t.message!!)
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                progress.dismiss()
                if(response.isSuccessful){
                        if (response.body()!!.success == 1){
                            showSuccessDialog("berhasil"){
                                alertDialog!!.dismiss()
                                tvStatusDetailTransfer.text = "DIBAYAR"
                                onBackPressed()
                            }

                        } else {
                            showErrorDialog(response.body()!!.message)
                        }

                } else {
                    showErrorDialog(response.message())
                }
            }


        })
    }

    fun batalCheckout(){
        val loading = SweetAlertDialog(this@DetailTransferActivity, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Memuat...").show()
        ApiConfig.instanceRetrofit.batalCheckout(transaksi.id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1){

                    SweetAlertDialog(this@DetailTransferActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil")
                        .setContentText("Transaksi berhasil dibatalkan")
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

    fun diterimaCheckout(){
        val loading = SweetAlertDialog(this@DetailTransferActivity, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Memuat...").show()
        ApiConfig.instanceRetrofit.selesaiCheckout(transaksi.id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1){

                    SweetAlertDialog(this@DetailTransferActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil")
                        .setContentText("Proses Pembelian telah anda selesaikan")
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
        tvTanggalDetailTransfer.text = Helper().convertDate(transaksi.created_att, newFormat)
        /*tvTanggalDetailTransfer.text = transaksi.created_att*/

        tvStatusDetailTransfer.text = transaksi.status
        tvPenerimaDetailTransfer.text = transaksi.name + " - " + transaksi.phone
        tvAlamatDetailTransfer.text = transaksi.detail_lokasi
        tvKodeUnikDetailTransfer.text = transaksi.kode_unik
        tvTotalBelanjaDetailTransfer.text = Helper().gantiRupiah(transaksi.total_harga)
        tvBiayaKirimDetailTransfer.text = Helper().gantiRupiah(transaksi.ongkir)
        tvTotalDetailTransfer.text = Helper().gantiRupiah(transaksi.total_transfer)

        if(transaksi.status != "MENUNGGU") {
            llBawah.visibility = View.GONE
        } else {
            llBawah.visibility = View.VISIBLE
        }

        if (transaksi.status != "DIKIRIM"){
            llSelesai.visibility = View.GONE
        } else {
            llSelesai.visibility = View.VISIBLE
        }

        var color = getColor(R.color.menunggu)
        if (transaksi.status == "SELESAI") color = getColor(R.color.selesai)
        else if (transaksi.status == "BATAL") color = getColor(R.color.batal)

        tvStatusDetailTransfer.setTextColor(color)
    }

    fun checkProses(){

    }

    fun displayDetailTransfer(transaksis: ArrayList<DetailTransaksi>) {

        val layoutManagerRiwayat = LinearLayoutManager(this)
        layoutManagerRiwayat.orientation = LinearLayoutManager.VERTICAL

        rvBarangDetailTransfer.adapter = BarangTransaksiAdapter(transaksis)
        rvBarangDetailTransfer.layoutManager = layoutManagerRiwayat

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}