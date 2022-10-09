package com.mavendra.golekkostv3.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.activity.KategoriBarangActivity
import com.mavendra.golekkostv3.activity.KategoriJasaangkutActivity
import com.mavendra.golekkostv3.activity.LoginActivity
import com.mavendra.golekkostv3.activity.RiwayatJualBarangActivity
import com.mavendra.golekkostv3.adapter.BarangAdapter
import com.mavendra.golekkostv3.adapter.JasaAngkutAdapter
import com.mavendra.golekkostv3.adapter.KostKontrakanAdapter
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.model.Barang
import com.mavendra.golekkostv3.model.Jasaangkut
import com.mavendra.golekkostv3.model.Kostkontrakan
import com.mavendra.golekkostv3.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class BerandaFragment : Fragment() {

    lateinit var rvKostKontrakan: RecyclerView
    lateinit var ivJualBarangg: ImageView
    lateinit var mcBarang: CardView
    lateinit var mcJasaangkut: CardView
    lateinit var s :SharedPref



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.fragment_beranda, container, false)

        init(view)

        getKostKontrakan()
        /*getJasaAngkut()*/
        /*getBarang()*/

        mainButton()

        s = SharedPref(requireActivity())
        prefBeranda()

        return view
    }

    private fun mainButton(){
        ivJualBarangg.setOnClickListener {
            startActivity(Intent(requireActivity(), RiwayatJualBarangActivity::class.java))
        }

        mcBarang.setOnClickListener {
            startActivity(Intent(requireActivity(), KategoriBarangActivity::class.java))
            /*Animatoo.animateSlideUp(requireActivity())*/
        }

        mcJasaangkut.setOnClickListener {
            startActivity(Intent(requireActivity(), KategoriJasaangkutActivity::class.java))
            /*Animatoo.animateSlideUp(requireActivity())*/
        }
    }

    private fun prefBeranda(){
        if (s.getUser() == null){
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }


    }

    /*fun getBarang(){
        ApiConfig.instanceRetrofit.getBarang().enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1){
                    listBarang = res.barangs
                    displayBarang()
                }
            }
        })

    }*/

    private var listKostKontrakan :ArrayList<Kostkontrakan> = ArrayList()
    fun getKostKontrakan(){
        ApiConfig.instanceRetrofit.getKostKontrakan().enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1){
                    listKostKontrakan = res.kostkontrakans
                    displayKostKontrakan()
                }
            }
        })

    }

    /*fun getJasaAngkut(){
        ApiConfig.instanceRetrofit.getJasaAngkut().enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1){
                    listJasaangkut = res.jasaangkuts
                    displayJasaAngkut()
                }
            }
        })

    }*/

    /*fun displayBarang(){
        val layoutManagerBarang = LinearLayoutManager(activity)
        layoutManagerBarang.orientation = LinearLayoutManager.HORIZONTAL

        rvBarangJualan.adapter = BarangAdapter(requireActivity(), listBarang)
        rvBarangJualan.layoutManager = layoutManagerBarang

    }*/

    fun displayKostKontrakan(){
        val layoutManagerKost = LinearLayoutManager(activity)
        layoutManagerKost.orientation = LinearLayoutManager.VERTICAL

        rvKostKontrakan.adapter = KostKontrakanAdapter(requireActivity(), listKostKontrakan)
        rvKostKontrakan.layoutManager = layoutManagerKost

    }

    /*fun displayJasaAngkut(){
        val layoutManagerJasaAngkut = LinearLayoutManager(activity)
        layoutManagerJasaAngkut.orientation = LinearLayoutManager.VERTICAL

        rvJasaAngkut.adapter = JasaAngkutAdapter(requireActivity(),listJasaangkut)
        rvJasaAngkut.layoutManager = layoutManagerJasaAngkut

    }*/

    private fun init(view: View) {
        mcBarang = view.findViewById(R.id.mcBarang)
        mcJasaangkut = view.findViewById(R.id.mcJasaangkut)
        ivJualBarangg = view.findViewById(R.id.ivJualBarang)
        /*rvBarangJualan = view.findViewById(R.id.rvBarangJualan)*/
        rvKostKontrakan = view.findViewById(R.id.rvKostKontrakan)
        /*rvJasaAngkut = view.findViewById(R.id.rvJasaAngkut)*/
    }
}