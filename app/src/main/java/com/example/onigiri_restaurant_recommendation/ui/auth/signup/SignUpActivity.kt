package com.example.onigiri_restaurant_recommendation.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.onigiri_restaurant_recommendation.databinding.ActivitySignUpBinding
import com.example.onigiri_restaurant_recommendation.model.User
import com.example.onigiri_restaurant_recommendation.ui.auth.login.LoginActivity
import com.example.onigiri_restaurant_recommendation.ui.survey.SurveyActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    companion object{
        private const val TAG = "SignupActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        }

        binding.btnSignup.setOnClickListener {
            signup()
        }
    }

    private fun signup() {
        with(binding) {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            val user = User(name, email, password)

            startActivity(
                Intent(this@SignUpActivity, SurveyActivity::class.java)
                    .putExtra(SurveyActivity.DATA_USER, user)
            )
        }
    }
}