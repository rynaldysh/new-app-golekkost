package com.mavendra.golekkostv3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.mavendra.golekkostv3.R

class WalkthroughAdapter(val context: Context): PagerAdapter(){

    val imgArray: IntArray = intArrayOf(R.drawable.wh_rumah, R.drawable.wh_jual_beli_barang, R.drawable.wh_jasa_angkut)
    val titleArray: Array<String> = arrayOf("Informasi" + "               Kost/Kontrakan",
        "Jual-Beli Perabotan Kost/Kontrakan", "Informasi Jasa Angkut/Pindahan")
    val descArray: Array<String> = arrayOf("Pengguna bisa mendapatkan informasi terkait kost/kontrakan",
        "Pengguna dapat menjual/membeli perabotan kost/kontrakan", "Pengguna bisa mendapatkan informasi terkait jasa angkut/pindahan")

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return imgArray.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(context).inflate(R.layout.slide_walkthrough, container, false)

        val txtTitle: TextView = view.findViewById(R.id.tv_title)
        val txtDesc: TextView = view.findViewById(R.id.tvDescription)
        val img: ImageView = view.findViewById(R.id.iv_img)

        txtTitle.text = titleArray[position]
        txtDesc.text = descArray[position]
        img.setImageDrawable(ContextCompat.getDrawable(context, imgArray[position]))
        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view: View = `object` as View
        container.removeView(view)
    }
}