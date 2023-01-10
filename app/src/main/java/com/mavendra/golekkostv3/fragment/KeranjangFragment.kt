package com.mavendra.golekkostv3.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.activity.MasukActivity
import com.mavendra.golekkostv3.activity.PengirimanActivity
import com.mavendra.golekkostv3.adapter.KeranjangAdapter
import com.mavendra.golekkostv3.helper.Helper
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.model.Barang
import com.mavendra.golekkostv3.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.toolbar_custom_fragment_keranjang_bawah.*

/**
 * A simple [Fragment] subclass.
 */
@SuppressLint("NotifyDataSetChanged")
class KeranjangFragment : Fragment() {

    lateinit var myDb: MyDatabase
    lateinit var s :SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_keranjang, container, false)

        init(view)
        myDb = MyDatabase.getInstance(requireActivity())!!
        s = SharedPref(requireActivity())

        displayBarang()
        mainButton()

        return view
    }

    lateinit var adapter: KeranjangAdapter
    var listBarang = ArrayList<Barang>()
    private fun displayBarang(){
        /*val myDb = MyDatabase.getInstance(requireActivity())*/
        listBarang = myDb.daoKeranjang().getAll() as ArrayList
        val layoutManagerBarang = LinearLayoutManager(activity)
        layoutManagerBarang.orientation = LinearLayoutManager.VERTICAL


        adapter = KeranjangAdapter(requireActivity(), listBarang, object  :KeranjangAdapter.Listeners{
            override fun onUpdate() {
                hitungTotal()
            }

            override fun onDelete(position: Int) {
                listBarang.removeAt(position)
                adapter.notifyDataSetChanged()
                hitungTotal()
            }

        })

        rvBarangJualan.adapter = adapter
        rvBarangJualan.layoutManager = layoutManagerBarang
    }

    var totalHarga = 0
    fun hitungTotal(){

        val listBarang = myDb.daoKeranjang().getAll() as ArrayList

        totalHarga = 0
        var isSelectedAll = true
        for (barang in listBarang){
            if (barang.selected){
                val harga = Integer.valueOf(barang.harga)
                totalHarga += (harga * barang.jumlah)
            } else {
                isSelectedAll = false
            }
        }

        cbAll.isChecked = isSelectedAll
        tvTotalHarga.text = Helper().gantiRupiah(totalHarga)
    }


    private fun mainButton(){
        ivDeleteKeranjang.setOnClickListener {
            val listDelete = ArrayList<Barang>()
            for (b in listBarang){
                if (b.selected) listDelete.add(b)
            }

            delete(listDelete)
        }

        btBayarKeranjang.setOnClickListener {

            if (s.getStatusLogin()){

                var isThereBarang = false

                for (b in listBarang){
                    if (b.selected) isThereBarang = true
                }

                if (isThereBarang){
                    val intent = Intent(requireActivity(), PengirimanActivity::class.java)
                    intent.putExtra("extra", "" + totalHarga)
                        startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "Tidak ada produk yang terpilih", Toast.LENGTH_SHORT).show()
                }

            } else {
                requireActivity().startActivity(Intent(requireActivity(), MasukActivity::class.java))
            }
        }

        cbAll.setOnClickListener {
            for(i in listBarang.indices){
                val barang = listBarang[i]
                barang.selected = cbAll.isChecked

                listBarang[i] = barang
            }

            adapter.notifyDataSetChanged()

        }
    }

    fun delete(data: ArrayList<Barang>){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().delete(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listBarang.clear()
                listBarang.addAll(myDb.daoKeranjang().getAll() as ArrayList)
                adapter.notifyDataSetChanged()
            })
    }

    lateinit var ivDeleteKeranjang: ImageView
    lateinit var rvBarangJualan: RecyclerView
    lateinit var tvTotalHarga: TextView
    lateinit var btBayarKeranjang: Button
    lateinit var cbAll: CheckBox

    private fun init(view: View) {
        ivDeleteKeranjang = view.findViewById(R.id.ivDeleteKeranjang)
        rvBarangJualan = view.findViewById(R.id.rvKostKontrakan)
        tvTotalHarga = view.findViewById(R.id.tvTotalHarga)
        btBayarKeranjang = view.findViewById(R.id.btBayarKeranjang)
        cbAll = view.findViewById(R.id.cbAll)
    }

    override fun onResume() {
        displayBarang()
        hitungTotal()
        super.onResume()
    }
}