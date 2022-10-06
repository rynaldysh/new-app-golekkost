package com.mavendra.golekkostv3.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.activity.*
import com.mavendra.golekkostv3.helper.SharedPref
import kotlinx.android.synthetic.main.fragment_profil.*

/**
 * A simple [Fragment] subclass.
 */
class ProfilFragment : Fragment() {

    lateinit var s :SharedPref
    lateinit var btLogout: Button
    lateinit var tvUsername :TextView
    lateinit var tvEmail :TextView
    lateinit var tvPhone :TextView
    lateinit var tvInisial :TextView
    lateinit var rlRiwayat :RelativeLayout
    lateinit var rlRiwayatJasa :RelativeLayout
    lateinit var rlRiwayatKostkontrakan :RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_profil, container, false)
        btLogout = view.findViewById(R.id.btLogout)
        init(view)

        s = SharedPref(requireActivity())

        mainButton()

        setData()
        return view
    }

    fun mainButton(){
        btLogout.setOnClickListener {
            s.setStatusLogin(false)
            startActivity(Intent(requireActivity(), MasukActivity::class.java))
        }
        rlRiwayat.setOnClickListener {
            startActivity(Intent(requireActivity(), RiwayatBelanjaActivity::class.java))
        }
        rlRiwayatJasa.setOnClickListener {
            startActivity(Intent(requireActivity(), RiwayatPesanJasaActivity::class.java))
        }
        rlRiwayatKostkontrakan.setOnClickListener {
            startActivity(Intent(requireActivity(), RiwayatPesanKostkontrakanActivity::class.java))
        }

    }

    fun init(view: View) {
        btLogout = view.findViewById(R.id.btLogout)
        tvUsername = view.findViewById(R.id.tv_username)
        tvEmail = view.findViewById(R.id.tv_email)
        tvPhone = view.findViewById(R.id.tv_phone)
        tvInisial = view.findViewById(R.id.tvProfil)
        rlRiwayat = view.findViewById(R.id.rlRiwayat)
        rlRiwayatJasa = view.findViewById(R.id.rlRiwayatJasa)
        rlRiwayatKostkontrakan = view.findViewById(R.id.rlRiwayatKostkontrakan)
    }

    private fun setData() {

        if (s.getUser() == null){
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }
        val user = s.getUser()!!
        tvUsername.text = user.name
        tvEmail.text = user.email
        tvPhone.text = user.phone
        tvInisial.text = user.name.getInitial()
    }

    private fun String?.getInitial(): String {
        if (this.isNullOrEmpty()) return ""
        val array = this.split(" ")
        if (array.isEmpty()) return this
        var inisial = array[0].substring(0, 1)
        if (array.size > 1) inisial += array[1].substring(0, 1)
        return inisial.uppercase()
    }




}