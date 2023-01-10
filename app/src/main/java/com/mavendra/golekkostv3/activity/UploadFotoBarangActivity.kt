package com.mavendra.golekkostv3.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.github.drjacky.imagepicker.ImagePicker
import com.google.gson.Gson
import com.inyongtisto.myhelper.base.BaseActivity
import com.inyongtisto.myhelper.extension.showErrorDialog
import com.inyongtisto.myhelper.extension.showSuccessDialog
import com.inyongtisto.myhelper.extension.toMultipartBody
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_transfer.*
import kotlinx.android.synthetic.main.activity_jual_barang.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_upload_foto_barang.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import kotlinx.android.synthetic.main.view_upload_bukti.*
import kotlinx.android.synthetic.main.view_upload_gambar_barang.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadFotoBarangActivity : BaseActivity() {

    var barang = Barang()

    lateinit var imageView: ImageView
    lateinit var btUplaod: Button
    lateinit var btTake: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_foto_barang)

        Helper().setToolbarSecond(this, toolbarBiasa, "Upload Foto Barang")

        val json = intent.getStringExtra("barangpush")
        barang = Gson().fromJson(json, Barang::class.java)

        mainButton()

    }

    fun mainButton(){
        btUploadGambarBarang.setOnClickListener {
            imagePick()
        }

    }

    var alertDialog : AlertDialog? = null

    @SuppressLint("inflateParams")
    private fun dialodUpload(file: File){

        val view = layoutInflater
        val layout = view.inflate(R.layout.view_upload_gambar_barang, null)

        val imageView : ImageView = layout.findViewById(R.id.imageBarang)
        val btUplaod : Button = layout.findViewById(R.id.btUploadBarang)
        val btTake : Button = layout.findViewById(R.id.btAmbilGambarBarang)

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

    private var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val uri = it.data?.data!!
            Log.d("TAG", "URI IMAGE: "+uri)
            val fileUri: Uri = uri
            dialodUpload(File(fileUri.path))
        }
    }


    private fun upload(file: File){

        val fileImage = file.toMultipartBody()

        progress.show()
        ApiConfig.instanceRetrofit.uploadBarangFoto(barang.id, fileImage!!).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                progress.dismiss()
                showErrorDialog(t.message!!)
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                progress.dismiss()
                if(response.isSuccessful){
                    if (response.body()!!.success == 1){

                        val jsJualBarang = Gson().toJson(barang, Barang::class.java)

                        val intent = Intent(this@UploadFotoBarangActivity, RiwayatJualBarangActivity::class.java)
                        intent.putExtra("barangpush", jsJualBarang)

                        showSuccessDialog("Data produk sudah lengkap, terimakasih!"){
                            alertDialog!!.dismiss()
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


}
