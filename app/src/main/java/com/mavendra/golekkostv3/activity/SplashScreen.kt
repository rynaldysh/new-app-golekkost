package com.mavendra.golekkostv3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.mavendra.golekkostv3.MainActivity
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.helper.SharedPrefOnBoarding

class SplashScreen : AppCompatActivity() {
    lateinit var pre: SharedPrefOnBoarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        pre = SharedPrefOnBoarding(this)

        Handler(Looper.getMainLooper()).postDelayed({
            var i = Intent()

            if (pre.firstInstall == false){
                i = Intent(this, WalkThroughActivity::class.java)
            } else{
                i = Intent(this, MainActivity::class.java)
            }

            startActivity(i)
            finish()
        },2000)
    }
}