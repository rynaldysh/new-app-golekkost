package com.mavendra.golekkostv3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mavendra.golekkostv3.activity.MasukActivity
import com.mavendra.golekkostv3.fragment.BerandaFragment
import com.mavendra.golekkostv3.fragment.DisimpanFragment
import com.mavendra.golekkostv3.fragment.KeranjangFragment
import com.mavendra.golekkostv3.fragment.ProfilFragment
import com.mavendra.golekkostv3.fragment.TabDisimpanFragment.DisimpanJasaFragment
import com.mavendra.golekkostv3.fragment.TabDisimpanFragment.DisimpanKostKontrakanFragment
import com.mavendra.golekkostv3.helper.SharedPref

class MainActivity : AppCompatActivity() {

    val fragmentBeranda: Fragment = BerandaFragment()
    val fragmenKeranjang: Fragment = KeranjangFragment()
    val fragmentDisimpan: Fragment = DisimpanFragment()
    val fragmentProfil: Fragment = ProfilFragment()
    val fm: FragmentManager = supportFragmentManager
    var active: Fragment = fragmentBeranda

    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var s:SharedPref

    private var dariDetail :Boolean = false
    private var dariDetailJasa :Boolean = false
    private var dariDetailKostkontrakan :Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        s = SharedPref(this)

        setUpButtonNav()
        /*

        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_beranda, R.id.nav_chat, R.id.nav_transaksi, R.id.nav_profil
        ).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_bottom.setupWithNavController(navController)

         */

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessage, IntentFilter("event:keranjang"))
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessagejasa, IntentFilter("event:simpanjasa"))
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageekostkontrakan, IntentFilter("event:simpankostkontrakan"))
    }

    val mMessage: BroadcastReceiver = object  :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            dariDetail = true
        }

    }

    val mMessagejasa: BroadcastReceiver = object  :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            dariDetailJasa = true
        }

    }

    val mMessageekostkontrakan: BroadcastReceiver = object  :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            dariDetailKostkontrakan = true
        }

    }

    private fun setUpButtonNav() {
        fm.beginTransaction().add(R.id.container, fragmentBeranda).show(fragmentBeranda).commit()
        fm.beginTransaction().add(R.id.container, fragmenKeranjang).hide(fragmenKeranjang).commit()
        fm.beginTransaction().add(R.id.container, fragmentDisimpan).hide(fragmentDisimpan).commit()
        fm.beginTransaction().add(R.id.container, fragmentProfil).hide(fragmentProfil).commit()

        bottomNavigationView = findViewById(R.id.nav_bottom)
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnItemSelectedListener { item ->

            when(item.itemId){
                R.id.nav_beranda ->{
                    if (s.getStatusLogin()){
                        callFragment(0, fragmentBeranda)
                    } else {
                        startActivity(Intent(this, MasukActivity::class.java))
                    }
                }
                R.id.nav_keranjang ->{
                    callFragment(1, fragmenKeranjang)
                }
                R.id.nav_disimpan ->{
                    callFragment(2, fragmentDisimpan)
                }
                R.id.nav_profil ->{
                    if (s.getStatusLogin()){
                        callFragment(3, fragmentProfil)
                    } else {
                        startActivity(Intent(this, MasukActivity::class.java))
                    }
                }
            }

            false

        }
    }

    fun callFragment(int: Int, fragment: Fragment) {
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment

    }

    override fun onResume() {
        if(dariDetail){
            dariDetail = false
            callFragment(1, fragmenKeranjang)
        }

        if (dariDetailKostkontrakan){
            dariDetailKostkontrakan = false
            callFragment(2, fragmentDisimpan)
        }

        if (dariDetailJasa){
            dariDetailJasa = false
            callFragment(2, fragmentDisimpan)
        }

        super.onResume()
    }

}