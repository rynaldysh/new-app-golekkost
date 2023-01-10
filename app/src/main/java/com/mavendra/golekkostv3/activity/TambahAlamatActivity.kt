package com.mavendra.golekkostv3.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.app.ApiConfigAlamat
import com.mavendra.golekkostv3.app.ApiKey
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.model.Alamat
import com.mavendra.golekkostv3.model.AlamatModel
import com.mavendra.golekkostv3.model.ResponModel
import com.mavendra.golekkostv3.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_tambah_alamat.*
import kotlinx.android.synthetic.main.toolbar_biasa.*
import kotlinx.android.synthetic.main.toolbar_custom_bottom_tambah_alamat_checkout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TambahAlamatActivity : AppCompatActivity() {

    var provinsi = AlamatModel.Provinsi()
    var kota = AlamatModel.Provinsi()
    var kecamatan = AlamatModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_alamat)

        Helper().setToolbar(this, toolbarBiasa, "Tambah alamat")

        getProvinsi()
        mainButton()

    }

    private fun mainButton() {
        btSimpan.setOnClickListener {
            simpan()
        }
    }

    private fun simpan(){
        when {
            etNama.text.isEmpty() -> {
                error(etNama)
                return
            }
            etType.text.isEmpty() -> {
                error(etType)
                return
            }
            etPhone.text.isEmpty() -> {
                error(etPhone)
                return
            }
            etAlamat.text.isEmpty() -> {
                error(etAlamat)
                return
            }
            etKodepos.text.isEmpty() -> {
                error(etKodepos)
                return
            }
        }

        /*if (provinsi.province_id == "0" ){
            toast("Silahkan pilih provinsi DI Yogyakarta")
            return
        }*/

        if (kota.city_id == "0" ){
            toast("Silahkan pilih kota/kabupaten")
            return
        }

        /*if (kecamatan.id == 0 ){
            toast("Silahkan pilih kecamatan")
            return
        }*/

        val alamat = Alamat()
        alamat.name = etNama.text.toString()
        alamat.type = etType.text.toString()
        alamat.phone = etPhone.text.toString()
        alamat.alamat = etAlamat.text.toString()
        alamat.kodepos = etKodepos.text.toString()

        alamat.id_provinsi = Integer.valueOf(provinsi.province_id)
        alamat.provinsi = provinsi.province

        alamat.id_kota = Integer.valueOf(kota.city_id)
        alamat.kota = kota.city_name

        /*alamat.id_kecamatan = kecamatan.id
        alamat.kecamatan = kecamatan.nama*/

        insert(alamat)
    }

    fun error(editText: EditText){
        editText.error = "Kolom tidak boleh kosong"
        editText.requestFocus()
    }

    fun toast(string: String){
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun getProvinsi(){
        ApiConfigAlamat.instanceRetrofit.getProvinsi(ApiKey.key).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful){

                    pb.visibility = View.GONE
                    divProvinsi.visibility = View.VISIBLE

                    val res = response.body()!!
                    val listProvinsi = res.rajaongkir.results

                    val arrayString = ArrayList<String>()
                    arrayString.add("Provinsi DI Yogyakarta")
                    for (prov in listProvinsi){
                        arrayString.add(prov.province)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arrayString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spnProvinsi.adapter = adapter

                    spnProvinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 4){
                                provinsi = listProvinsi[4]
                                val idProv = provinsi.province_id
                                getKota(idProv)
                            }
                        }
                    }

                } else {
                    Log.d("Error", "gagal memuat data" + response.message())
                }

            }
        })
    }

    private fun getKota(id: String){
        ApiConfigAlamat.instanceRetrofit.getKota(ApiKey.key, id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful){

                    pb.visibility = View.GONE
                    divKabupatenKota.visibility = View.VISIBLE

                    val res = response.body()!!
                    val listKota = res.rajaongkir.results
                    val arrayString = ArrayList<String>()
                    arrayString.add("Pilih Kota/Kabupaten")
                    for (kota in listKota){
                        arrayString.add(kota.type + "" + kota.city_name)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arrayString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spnKota.adapter = adapter

                    spnKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0){
                                kota = listKota[position - 1]
                                val kodePos = kota.postal_code
                                etKodepos.setText(kodePos)
                                /*getKecamatan(idKota)*/
                            }
                        }
                    }

                } else {
                    Log.d("Error", "gagal memuat data" + response.message())
                }

            }
        })
    }

    /*private fun getKecamatan(id: Int){
        ApiConfigAlamat.instanceRetrofit.getKecamatan(id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful){

                    pb.visibility = View.GONE
                    divKecamatan.visibility = View.VISIBLE

                    val res = response.body()!!
                    val listKecamatan = res.kecamatan
                    val arrayString = ArrayList<String>()
                    arrayString.add("Pilih Kecamatan")
                    for (prov in listKecamatan){
                        arrayString.add(prov.nama)
                    }

                    val jasaAdapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arrayString.toTypedArray())
                    jasaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spnKecamatan.jasaAdapter = jasaAdapter

                    spnKecamatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0){
                                kecamatan = listKecamatan[position - 1]
                                val idKecamatan = listKecamatan[position - 1].id
                                getKota(idKecamatan)
                            }
                        }
                    }

                } else {
                    Log.d("Error", "gagal memuat data" + response.message())
                }

            }
        })
    }*/

    fun insert(data: Alamat){
        val myDb = MyDatabase.getInstance(this)!!
        if (myDb.daoAlamat().getByStatus(true) == null){
            data.isSelected = true
        }
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().insert(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                toast("Insert Data berhasil")
                onBackPressed()
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}