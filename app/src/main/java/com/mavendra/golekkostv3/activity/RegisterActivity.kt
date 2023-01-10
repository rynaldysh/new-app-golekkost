package com.mavendra.golekkostv3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.mavendra.golekkostv3.R
import com.mavendra.golekkostv3.app.ApiConfig
import com.mavendra.golekkostv3.helper.SharedPref
import com.mavendra.golekkostv3.model.ResponModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.etEmail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    lateinit var s: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        s = SharedPref(this)

        btRegister.setOnClickListener {
            register()
        }

        tvMasuk.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun register() {
        val username = etName.text.toString().trim()
        val phone = etTelfon.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val konfirmasiSandi = etKonfirmasi.text.toString().trim()
        if (username.isEmpty()){
            etName.error = "Kolom nama tidak boleh kosong"
            etName.requestFocus()
            return
        } else if (phone.isEmpty()){
            etTelfon.error = "Kolom nomor telfon tidak boleh kosong"
            etTelfon.requestFocus()
            return
        } else if (email.isEmpty()){
            etEmail.error = "Kolom email tidak boleh kosong"
            etEmail.requestFocus()
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.error = "Email tidak valid"
            etEmail.requestFocus()
            return
        }else if (password.isEmpty() || password.length > 8){
            etPassword.error = "Password harus lebih dari 8 karakter"
            etPassword.requestFocus()
            return
        } else if(konfirmasiSandi != password){
            etKonfirmasi.error = "Password tidak sama"
            etKonfirmasi.requestFocus()
            return
        }

        //pb.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.register(username, phone, email, password).enqueue(object :
            Callback<ResponModel> {

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                //pb.visibility = View.GONE
                Toast.makeText(this@RegisterActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                //pb.visibility = View.GONE
                val respon = response.body()!!
                if (respon.success == 1){
                    s.setStatusLogin(true)
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@RegisterActivity, "" + respon.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "" + respon.message, Toast.LENGTH_SHORT).show()
                }

            }
        })

    }
}