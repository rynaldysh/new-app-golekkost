package com.mavendra.golekkostv3.fragment.TabDisimpanFragment

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
import com.mavendra.golekkostv3.activity.KirimPemesananJasaActivity
import com.mavendra.golekkostv3.activity.MasukActivity
import com.mavendra.golekkostv3.adapter.SimpanJasaAdapter
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.model.Jasaangkut
import com.mavendra.golekkostv3.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * A simple [Fragment] subclass.
 */
@SuppressLint("NotifyDataSetChanged")
class DisimpanJasaFragment : Fragment() {

    lateinit var myDb: MyDatabase
    lateinit var s :SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_disimpan_jasa, container, false)

        myDb = MyDatabase.getInstance(requireActivity())!!
        s = SharedPref(requireActivity())

        init(view)
        displayJasa()
        mainButton()

        return view
    }

    lateinit var jasaAdapter: SimpanJasaAdapter
    var listJasaangkut = ArrayList<Jasaangkut>()
    private fun displayJasa(){
        /*val myDb = MyDatabase.getInstance(requireActivity())*/
        listJasaangkut = myDb.daoSimpanJasa().getAll() as ArrayList
        val layoutManagerJasaangkut = LinearLayoutManager(activity)
        layoutManagerJasaangkut.orientation = LinearLayoutManager.VERTICAL


        jasaAdapter = SimpanJasaAdapter(requireActivity(), listJasaangkut, object  : SimpanJasaAdapter.Listeners{
            override fun onUpdate() {
                centangTotal()
            }

            override fun onDelete(position: Int) {
                listJasaangkut.removeAt(position)
                jasaAdapter.notifyDataSetChanged()
                centangTotal()
            }

        })

        rvDisimpanJasa.adapter = jasaAdapter
        rvDisimpanJasa.layoutManager = layoutManagerJasaangkut
    }

    private fun centangTotal(){

        val listJasaangkut = myDb.daoSimpanJasa().getAll() as ArrayList

        var isSelectedAll = true

        for (jasaangkut in listJasaangkut){
            isSelectedAll = jasaangkut.selected
        }

        cbAllDisimpanJasa.isChecked = isSelectedAll
    }

    private fun mainButton(){

        ivDeleteDisimpanJasa.setOnClickListener {
            val listDelete = ArrayList<Jasaangkut>()
            for (b in listJasaangkut){
                if (b.selected) listDelete.add(b)
            }

            delete(listDelete)
        }

        btPesanJasaBawah.setOnClickListener {

            if (s.getStatusLogin()){

                var isThereDisimpanJasa = false

                for (b in listJasaangkut){
                    if (b.selected) isThereDisimpanJasa = true
                }

                if (isThereDisimpanJasa){
                    val intent = Intent(requireActivity(), KirimPemesananJasaActivity::class.java)
                    intent.putExtra("extrajasa", "")
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "Tidak ada jasa yang terpilih", Toast.LENGTH_SHORT).show()
                }

            } else {
                requireActivity().startActivity(Intent(requireActivity(), MasukActivity::class.java))
            }
        }

        cbAllDisimpanJasa.setOnClickListener {
            for(i in listJasaangkut.indices){
                val jasa = listJasaangkut[i]
                jasa.selected = cbAllDisimpanJasa.isChecked

                listJasaangkut[i] = jasa
            }

            jasaAdapter.notifyDataSetChanged()

        }
    }

    fun delete(data: ArrayList<Jasaangkut>){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoSimpanJasa().delete(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listJasaangkut.clear()
                listJasaangkut.addAll(myDb.daoSimpanJasa().getAll() as ArrayList)
                jasaAdapter.notifyDataSetChanged()
            })
    }

    lateinit var rvDisimpanJasa: RecyclerView
    lateinit var ivDeleteDisimpanJasa: ImageView
    lateinit var cbAllDisimpanJasa: CheckBox
    lateinit var btPesanJasaBawah: Button

    private fun init(view: View) {
        ivDeleteDisimpanJasa = view.findViewById(R.id.ivDeleteDisimpanJasa)
        rvDisimpanJasa = view.findViewById(R.id.rvListDisimpanJasa)
        cbAllDisimpanJasa = view.findViewById(R.id.cbAllDisimpanJasa)
        btPesanJasaBawah = view.findViewById(R.id.btPesanJasaBawah)
    }

    override fun onResume() {
        displayJasa()
        centangTotal()
        super.onResume()
    }
}