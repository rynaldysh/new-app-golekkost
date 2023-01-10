package com.mavendra.golekkostv3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.adapter.WalkthroughAdapter
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.helper.SharedPrefOnBoarding
import kotlinx.android.synthetic.main.activity_walk_through.*

class WalkThroughActivity : AppCompatActivity() {

    lateinit var wkAdapter: WalkthroughAdapter
    val dots = arrayOfNulls<TextView>(3)
    var currentpage: Int = 0
    lateinit var pre: SharedPrefOnBoarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk_through)

        pre = SharedPrefOnBoarding(this)
        wkAdapter = WalkthroughAdapter(this)
        vpWalkThrough.adapter = wkAdapter
        dotIndicator(currentpage)

        initAction()
    }

    fun initAction(){
        vpWalkThrough.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
                Log.e("bbbb", state.toString())
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                dotIndicator(position)
                currentpage = position
            }
        })

        tvLanjutkan.setOnClickListener {
            if (vpWalkThrough.currentItem + 1 <dots.size){
                vpWalkThrough.currentItem += 1
            } else {
                pre.firstInstall = true
                val lanjutkanWt = Intent(this, LoginActivity::class.java)
                startActivity(lanjutkanWt)
                finish()
            }
        }

        tvLewati.setOnClickListener {
            pre.firstInstall = true
            val lewatiWt = Intent(this, MasukActivity::class.java)
            startActivity(lewatiWt)
            finish()
        }
    }

    fun dotIndicator(p: Int){
        llDots.removeAllViews()

        for(i in 0..dots.size-1){
            dots[i] = TextView(this)
            dots[i]?.textSize = 35f
            dots[i]?.setTextColor(resources.getColor(R.color.bg_color_primary))
            dots[i]?.text = Html.fromHtml("&#8226")

            llDots.addView(dots[i])
        }

        if (dots.size > 0){
            dots[p]?.setTextColor(resources.getColor(R.color.bg_color_splash))
        }
    }
}