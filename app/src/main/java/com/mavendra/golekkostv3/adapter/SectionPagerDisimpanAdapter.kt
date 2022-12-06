package com.mavendra.golekkostv3.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mavendra.golekkostv3.fragment.TabDisimpanFragment.DisimpanKostKontrakanFragment
import com.mavendra.golekkostv3.fragment.TabDisimpanFragment.DisimpanJasaFragment

class SectionPagerDisimpanAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Kost atau Kontrakan"
            1 -> "Jasa Angkut"
            else -> ""
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        var fragment : Fragment
        return  when(position){
            0 -> {
                fragment = DisimpanKostKontrakanFragment()
                return  fragment
            }
            1 -> {
                fragment = DisimpanJasaFragment()
                return  fragment
            }
            else -> {
                fragment = DisimpanKostKontrakanFragment()
                return fragment
            }
        }
    }
}