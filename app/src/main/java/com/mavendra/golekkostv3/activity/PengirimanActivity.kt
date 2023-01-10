package com.mavendra.golekkostv3.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.KurirAdapter
import com.mavendra.golekkostv3.app.ApiConfigAlamat
import com.mavendra.golekkostv3.app.ApiKey
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.model.Barang
import com.mavendra.golekkostv3.model.CheckOut
import com.mavendra.golekkostv3.model.rajaongkir.Costs
import com.mavendra.golekkostv3.model.rajaongkir.ResponOngkir
import com.mavendra.golekkostv3.room.MyDatabase
import kotlinx.android.synthetic.main.activity_pengiriman.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class PengirimanActivity : AppCompatActivity() {

    lateinit var myDb: MyDatabase
    var totalHarga = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengiriman)

        myDb = MyDatabase.getInstance(this)!!
        Helper().setToolbar(this, toolbarBiasa, "Detail Transaksi")

        totalHarga = Integer.valueOf(intent.getStringExtra("extra")!!)
        tvTotalBelanja.text = Helper().gantiRupiah(totalHarga)

        mainButton()
        setSpinner()

    }

    fun setSpinner(){
        val arrayString = ArrayList<String>()
        arrayString.add("JNE")
        arrayString.add("POS")
        arrayString.add("TIKI")

        val adapter = ArrayAdapter<Any>(this, R.layout.item_spinner, arrayString.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnKurir.adapter = adapter

        spnKurir.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0){
                    getOngkir(spnKurir.selectedItem.toString())
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    fun checkAlamat(){
        if (myDb.daoAlamat().getByStatus(true) != null){
            cvAlamat.visibility = View.VISIBLE
            tvKosong.visibility = View.GONE
            llMetodePengiriman.visibility = View.VISIBLE

            btBayar.visibility = View.VISIBLE
            spnKurir.visibility = View.VISIBLE

            val a = myDb.daoAlamat().getByStatus(true)!!
            tvNamaAlamat.text = a.name
            tvPhoneAlamat.text = a.phone
            tvAlamatAlamat.text = a.alamat + ", " + a.kota + ", " + a.kodepos + ", (" + a.type + ")"
            btTambahAlamat.text = "Ubah Alamat"

            rvMetode.visibility = View.VISIBLE
            getOngkir("JNE")

        } else {
            cvAlamat.visibility = View.GONE
            tvKosong.visibility = View.VISIBLE
            btBayar.visibility = View.GONE
            spnKurir.visibility = View.GONE
            btTambahAlamat.text = "Tambah Alamat"
        }
    }

    private fun mainButton() {
        btTambahAlamat.setOnClickListener {
            startActivity(Intent(this, ListAlamatActivity::class.java))
        }
        btBayar.setOnClickListener {
            bayar()

        }
    }

    private fun bayar() {

        val user = SharedPref(this).getUser()!!
        val a = myDb.daoAlamat().getByStatus(true)!!

        var totalItem = 0
        val listBarang = myDb.daoKeranjang().getAll() as ArrayList
        var totalHarga = 0
        val barangs = ArrayList<CheckOut.Item>()

        for (b in listBarang){
            if (b.selected){
                totalItem += b.jumlah
                totalHarga += (b.jumlah * Integer.valueOf(b.harga))

                val barang = CheckOut.Item()
                barang.id = "" + b.id
                barang.total_item = "" + b.jumlah
                barang.total_harga = "" + (b.jumlah * Integer.valueOf(b.harga))
                barang.catatan = ""

                barangs.add(barang)
            }
        }

        val checkout = CheckOut()
        checkout.usergeneral_id = "" + user.id
        checkout.total_item = "" + totalItem
        checkout.total_harga = "" + totalHarga
        checkout.name = a.name
        checkout.phone = a.phone
        checkout.detail_lokasi = tvAlamatAlamat.text.toString()
        checkout.jasa_pengiriman = jasaKirim
        checkout.ongkir = ongkir
        checkout.kurir = kurir
        checkout.total_transfer = "" + (totalHarga + Integer.valueOf(ongkir))
        checkout.barangs = barangs

        val json = Gson().toJson(checkout, CheckOut::class.java)
        Log.d("Respon: ", "jvson:" + json)

        val intent = Intent(this, PembayaranActivity::class.java)
        intent.putExtra("extra", json)
        startActivity(intent)


    }

    private fun getOngkir(kurir: String){

        val alamat = myDb.daoAlamat().getByStatus(true)

        val origin = "501"
        val destination = "" + alamat!!.id_kota.toString()
        val berat = 1000

        ApiConfigAlamat.instanceRetrofit.ongkir(ApiKey.key, origin, destination, berat, kurir.toLowerCase()).enqueue(object :Callback<ResponOngkir> {
            override fun onResponse(call: Call<ResponOngkir>, response: Response<ResponOngkir>) {

                if (response.isSuccessful){

                    Log.d("Succes", "Berhasil memuat data")
                    val result = response.body()!!.rajaongkir.results
                    if (result.isNotEmpty()){
                        displayOngkir(result[0].code.toUpperCase(), result[0].costs)
                    }
                } else {
                    Log.d("Error", "gagal memuat data" + response.message())
                }

            }

            override fun onFailure(call: Call<ResponOngkir>, t: Throwable) {
                Log.d("Error", "gagal memuat data: " + t.message)

            }

        })
    }

    var ongkir = "0"
    var kurir = ""
    var jasaKirim = ""
    private fun displayOngkir(kurirr: String, arrayList: ArrayList<Costs>) {

        var arrayOngkir = ArrayList<Costs>()
        for (i in arrayList.indices){
            val ongkir = arrayList[i]
            if (i == 0){
                ongkir.isActive = true
            }
            arrayOngkir.add(ongkir)
        }

        setTotal(arrayOngkir[0].cost[0].value)

        ongkir = arrayOngkir[0].cost[0].value
        kurir = kurirr
        jasaKirim = arrayOngkir[0].service

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        var adapter :KurirAdapter? = null
        adapter = KurirAdapter(arrayOngkir, kurirr, object : KurirAdapter.Listeners {
            @SuppressLint("NotifyDataSetChanged")
            override fun onClicked(data: Costs, index: Int) {
                val newArrayOngkir = ArrayList<Costs>()
                for (ongkir in arrayOngkir){
                    ongkir.isActive = data.description == ongkir.description
                    newArrayOngkir.add(ongkir)
                }
                arrayOngkir = newArrayOngkir
                adapter!!.notifyDataSetChanged()
                setTotal(data.cost[0].value)

                ongkir = data.cost[0].value
                kurir = kurirr
                jasaKirim = data.service
            }
        })
        rvMetode.adapter = adapter
        rvMetode.layoutManager = layoutManager
    }

    fun setTotal(ongkir :String){
        tvBiayaPengiriman.text = Helper().gantiRupiah(ongkir)
        tvTotalKeseleruhan.text = Helper().gantiRupiah(Integer.valueOf(ongkir) + totalHarga)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        checkAlamat()
        super.onResume()
    }
}