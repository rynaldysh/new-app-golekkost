package com.mavendra.golekkostv3.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.mavendra.golekkostv3.MainActivity
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.CheckoutPesanJasa
import com.mavendra.golekkostv3.model.CheckoutPesanKostKontrakan
import com.mavendra.golekkostv3.room.MyDatabase
import kotlinx.android.synthetic.main.activity_succes_kost_kontrakan.*
import kotlinx.android.synthetic.main.toolbar_biasa.*

class SuccesKostkontrakanActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_succes_kost_kontrakan)

        Helper().setToolbar(this, toolbarBiasa, "Sukses Booking")

        setValue()
        mainButton()
    }

    fun mainButton(){
        btCekStatusKostKontrakan.setOnClickListener {
            startActivity(Intent(this, RiwayatPesanKostkontrakanActivity::class.java))
        }
    }

    fun setValue(){

        val jsCheckoutPesanKostkontrakan = intent.getStringExtra("bokingkostkontrakan")

        val checkoutPesanKostkontrakan = Gson().fromJson(jsCheckoutPesanKostkontrakan, CheckoutPesanKostKontrakan::class.java)

        val myDb = MyDatabase.getInstance(this)!!

        for(kostkontrakan in checkoutPesanKostkontrakan.kostkontrakans){
            myDb.daoSimpanKostKontrakan().deleteById(kostkontrakan.id)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }

}