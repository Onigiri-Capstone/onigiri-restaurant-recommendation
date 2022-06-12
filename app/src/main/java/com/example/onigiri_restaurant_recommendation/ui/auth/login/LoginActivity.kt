package com.example.onigiri_restaurant_recommendation.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.onigiri_restaurant_recommendation.data.remote.network.Firebase
import com.example.onigiri_restaurant_recommendation.databinding.ActivityLoginBinding
import com.example.onigiri_restaurant_recommendation.ui.auth.signup.SignUpActivity
import com.example.onigiri_restaurant_recommendation.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            signup.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }

            btnLogin.setOnClickListener {
                login()
            }

        }
    }

    private fun login() {
        with(binding) {

            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (email != "" && password != "") {
                Firebase.authInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()

                        } else {
                            Toast.makeText(this@LoginActivity, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else{
                Toast.makeText(this@LoginActivity, "Input email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}