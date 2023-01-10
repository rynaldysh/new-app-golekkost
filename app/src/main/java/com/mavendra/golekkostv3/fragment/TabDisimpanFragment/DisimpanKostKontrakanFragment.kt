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
import com.mavendra.golekkostv3.activity.KirimPemesananKostKontrakanActivity
import com.mavendra.golekkostv3.activity.MasukActivity
import com.mavendra.golekkostv3.adapter.SimpanKostKontrakanAdapter
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.model.Kostkontrakan
import com.mavendra.golekkostv3.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * A simple [Fragment] subclass.
 */
@SuppressLint("NotifyDataSetChanged")
class DisimpanKostKontrakanFragment : Fragment() {

    lateinit var myDb: MyDatabase
    lateinit var s :SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_disimpan_kost_kontrakan, container, false)

        myDb = MyDatabase.getInstance(requireActivity())!!
        s = SharedPref(requireActivity())

        init(view)
        displayKostKontrakan()
        mainButton()

        return view
    }

    lateinit var kostkontrakanAdapter: SimpanKostKontrakanAdapter
    var listKostKontrakan = ArrayList<Kostkontrakan>()
    private fun displayKostKontrakan(){
        /*val myDb = MyDatabase.getInstance(requireActivity())*/
        listKostKontrakan = myDb.daoSimpanKostKontrakan().getAll() as ArrayList
        val layoutManagerlistKostKontrakan = LinearLayoutManager(activity)
        layoutManagerlistKostKontrakan.orientation = LinearLayoutManager.VERTICAL


        kostkontrakanAdapter = SimpanKostKontrakanAdapter(requireActivity(), listKostKontrakan, object  : SimpanKostKontrakanAdapter.Listeners{
            override fun onUpdate() {
                centangTotal()
            }

            override fun onDelete(position: Int) {
                listKostKontrakan.removeAt(position)
                kostkontrakanAdapter.notifyDataSetChanged()
                centangTotal()
            }

        })

        rvDisimpanKostKontrakan.adapter = kostkontrakanAdapter
        rvDisimpanKostKontrakan.layoutManager = layoutManagerlistKostKontrakan
    }

    private fun centangTotal(){

        val listKostKontrakan = myDb.daoSimpanKostKontrakan().getAll() as ArrayList

        var isSelectedAll = true

        for (kostkontrakan in listKostKontrakan){
            isSelectedAll = kostkontrakan.selected
        }

        cbAllDisimpanKostKontrakan.isChecked = isSelectedAll
    }

    private fun mainButton(){

        ivDeleteDisimpanKostKontrakan.setOnClickListener {
            val listDelete = ArrayList<Kostkontrakan>()
            for (b in listKostKontrakan){
                if (b.selected) listDelete.add(b)
            }

            delete(listDelete)
        }

        btPesanKostKontrakanBawah.setOnClickListener {

            if (s.getStatusLogin()){

                var isThereDisimpanKostKontrakan = false

                for (b in listKostKontrakan){
                    if (b.selected) isThereDisimpanKostKontrakan = true
                }

                if (isThereDisimpanKostKontrakan){
                    val intent = Intent(requireActivity(), KirimPemesananKostKontrakanActivity::class.java)
                    intent.putExtra("extrakostkontrakan", "")
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "Tidak ada kost atau kontrakan yang terpilih", Toast.LENGTH_SHORT).show()
                }

            } else {
                requireActivity().startActivity(Intent(requireActivity(), MasukActivity::class.java))
            }
        }

        cbAllDisimpanKostKontrakan.setOnClickListener {
            for(i in listKostKontrakan.indices){
                val kostkontrakan = listKostKontrakan[i]
                kostkontrakan.selected = cbAllDisimpanKostKontrakan.isChecked

                listKostKontrakan[i] = kostkontrakan
            }

            kostkontrakanAdapter.notifyDataSetChanged()

        }
    }

    fun delete(data: ArrayList<Kostkontrakan>){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoSimpanKostKontrakan().delete(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listKostKontrakan.clear()
                listKostKontrakan.addAll(myDb.daoSimpanKostKontrakan().getAll() as ArrayList)
                kostkontrakanAdapter.notifyDataSetChanged()
            })
    }

    lateinit var rvDisimpanKostKontrakan: RecyclerView
    lateinit var ivDeleteDisimpanKostKontrakan: ImageView
    lateinit var cbAllDisimpanKostKontrakan: CheckBox
    lateinit var btPesanKostKontrakanBawah: Button

    private fun init(view: View) {
        ivDeleteDisimpanKostKontrakan = view.findViewById(R.id.ivDeleteDisimpanKostKontrakan)
        rvDisimpanKostKontrakan = view.findViewById(R.id.rvListDisimpanKostKontrakan)
        cbAllDisimpanKostKontrakan = view.findViewById(R.id.cbAllDisimpanKostKontrakan)
        btPesanKostKontrakanBawah = view.findViewById(R.id.btPesanKostKontrakanBawah)
    }

    override fun onResume() {
        displayKostKontrakan()
        centangTotal()
        super.onResume()
    }
}