package com.mavendra.golekkostv3.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.KeranjangAdapter
import com.mavendra.golekkostv3.adapter.SectionPagerAdapter
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.model.Jasaangkut
import com.mavendra.golekkostv3.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_disimpan.*

/**
 * A simple [Fragment] subclass.
 */

class DisimpanFragment : Fragment() {

    lateinit var myDb: MyDatabase
    lateinit var s :SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_disimpan, container, false)

        /*init(view)*/
        /*myDb = MyDatabase.getInstance(requireActivity())!!*/
        s = SharedPref(requireActivity())

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val sectionPagerAdapter = SectionPagerAdapter(
            childFragmentManager
        )
        vpSimpan.adapter = sectionPagerAdapter
        tab.setupWithViewPager(vpSimpan)
    }

}