package com.mavendra.golekkostv3.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.mavendra.golekkostv3.MainActivity
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.Bank
import com.mavendra.golekkostv3.model.CheckOut
import com.mavendra.golekkostv3.model.Transaksi
import com.mavendra.golekkostv3.room.MyDatabase
import kotlinx.android.synthetic.main.activity_succes.*
import kotlinx.android.synthetic.main.toolbar_biasa.*

class SuccesActivity : AppCompatActivity() {

    var nominal = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_succes)

        Helper().setToolbar(this, toolbarBiasa, "Detail Transfer")

        setValue()
        mainButton()
    }

    fun mainButton(){
        ivCopyRek.setOnClickListener {
            val cpManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val cpTxt = ClipData.newPlainText("text", tvNomorRekening.text.toString())
            cpManager.setPrimaryClip(cpTxt)

            Toast.makeText(this, "Nomor rekening berhasil di salin", Toast.LENGTH_LONG).show()
        }

        btCekStatus.setOnClickListener {
            startActivity(Intent(this, RiwayatBelanjaActivity::class.java))
        }
    }

    fun setValue(){

        val jsBank = intent.getStringExtra("bank")
        val jsTransaksi = intent.getStringExtra("transaksi")
        val jsCheckout = intent.getStringExtra("checkout")

        val bank = Gson().fromJson(jsBank, Bank::class.java)
        val transaksi = Gson().fromJson(jsTransaksi, Transaksi::class.java)
        val checkOut = Gson().fromJson(jsCheckout, CheckOut::class.java)

        val myDb = MyDatabase.getInstance(this)!!

        for(barang in checkOut.barangs){
            myDb.daoKeranjang().deleteById(barang.id)
        }

        tvNomorRekening.text = bank.rekening
        tvNamaPenerima.text = bank.penerima
        ivBank.setImageResource(bank.image)

        nominal = Integer.valueOf(transaksi.total_transfer)
        tvNominalTotal.text = Helper().gantiRupiah(nominal)
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