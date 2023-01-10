package com.mavendra.golekkostv3.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.activity.*
import com.mavendra.golekkostv3.adapter.KostKontrakanAdapter
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.model.*
import kotlinx.android.synthetic.main.item_aktivitas_barang.*
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
    lateinit var mcKostKontrakan: CardView
    lateinit var s :SharedPref

    lateinit var tvBA: RelativeLayout
    lateinit var tvTr: RelativeLayout
    lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_beranda, container, false)

        init(view)

        getKostKontrakan()
        setAktivitasJual()

        mainButton()

        s = SharedPref(requireActivity())
        prefBeranda()

        return view
    }

    private fun mainButton(){
        /*ivJualBarangg.setOnClickListener {
            startActivity(Intent(requireActivity(), JualBarangActivity::class.java))
        }*/

        mcBarang.setOnClickListener {
            startActivity(Intent(requireActivity(), KategoriBarangActivity::class.java))
        }

        mcJasaangkut.setOnClickListener {
            startActivity(Intent(requireActivity(), KategoriJasaangkutActivity::class.java))
        }

        mcKostKontrakan.setOnClickListener {
            startActivity(Intent(requireActivity(), KategoriKostKontrakanActivity::class.java))
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

    fun displayKostKontrakan(){
        val layoutManagerKost = LinearLayoutManager(activity)
        layoutManagerKost.orientation = LinearLayoutManager.VERTICAL

        rvKostKontrakan.adapter = KostKontrakanAdapter(requireActivity(), listKostKontrakan)
        rvKostKontrakan.layoutManager = layoutManagerKost

    }

    @SuppressLint("CutPasteId")
    private fun setAktivitasJual(){
        ivJualBarangg.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.item_aktivitas_barang, null)

            tvBA = dialogView.findViewById(R.id.rlBarangAnda)
            tvTr = dialogView.findViewById(R.id.rlTransaksiPenjualan)

            bottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheetDialog.setContentView(dialogView)
            bottomSheetDialog.show()

            tvBA.setOnClickListener {
                startActivity(Intent(requireActivity(), RiwayatJualBarangActivity::class.java))
            }

            tvTr.setOnClickListener {
                startActivity(Intent(requireActivity(), RiwayatTransaksiPenjualanActivity::class.java))
            }
        }
    }

    private fun init(view: View) {
        mcBarang = view.findViewById(R.id.mcBarang)
        mcJasaangkut = view.findViewById(R.id.mcJasaangkut)
        mcKostKontrakan = view.findViewById(R.id.mcKostKontrakan)
        ivJualBarangg = view.findViewById(R.id.ivJualBarang)
        rvKostKontrakan = view.findViewById(R.id.rvKostKontrakan)
    }
}