package com.mavendra.golekkostv3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mavendra.golekkostv3.MainActivity
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.model.ResponModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etEmail
import kotlinx.android.synthetic.main.activity_login.etPassword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var s: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        s = SharedPref(this)

        btMasuk.setOnClickListener {
            login()
        }

        tvDaftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    fun login() {
        /*
        val email = etEmail.text.toString().trim()
        val password = etKataSandi.text.toString().trim()

         */
        if (etEmail.text!!.isEmpty()){
            etEmail.error = "Kolom email tidak boleh kosong"
            etEmail.requestFocus()
            return
        } else if (etPassword.text!!.isEmpty()){
            etPassword.error = "Password harus lebih dari 8 karakter"
            etPassword.requestFocus()
            return
        }

        //pb.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.login(etEmail.text.toString(), etPassword.text.toString()).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                //pb.visibility = View.GONE
                Toast.makeText(this@LoginActivity, "Error:"+t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                //pb.visibility = View.GONE
                val respon = response.body()!!
                if (respon.success == 1){
                    s.setStatusLogin(true)
                    s.setUser(respon.usergeneral)
                    /*
                    s.setString(s.nama, respon.user.name)
                    s.setString(s.phone, respon.user.phone)
                    s.setString(s.email, respon.user.email)
                     */
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@LoginActivity, "Berhasil masuk, "+respon.usergeneral.name, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity, ""+respon.message, Toast.LENGTH_SHORT).show()
                }

            }
        })

    }
}
