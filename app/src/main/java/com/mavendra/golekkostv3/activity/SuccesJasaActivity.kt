package com.mavendra.golekkostv3.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.mavendra.golekkostv3.MainActivity
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.Barang
import com.mavendra.golekkostv3.model.CheckoutPesanJasa
import com.mavendra.golekkostv3.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_succes_jasa.*
import kotlinx.android.synthetic.main.activity_succes_kost_kontrakan.*
import kotlinx.android.synthetic.main.toolbar_biasa.*

class SuccesJasaActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_succes_jasa)

        Helper().setToolbar(this, toolbarBiasa, "Sukses Pemesanan")

        setValue()
        mainButton()
    }

    fun mainButton(){
        btCekStatusJasa.setOnClickListener {
            startActivity(Intent(this, RiwayatPesanJasaActivity::class.java))
        }
    }

    fun setValue(){

        val jsCheckoutPesanJasa = intent.getStringExtra("bokingjasa")

        val checkoutPesanJasa = Gson().fromJson(jsCheckoutPesanJasa, CheckoutPesanJasa::class.java)

        val myDb = MyDatabase.getInstance(this)!!

        for(jasaangkut in checkoutPesanJasa.jasaangkuts){
            myDb.daoSimpanJasa().deleteById(jasaangkut.id)
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