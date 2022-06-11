package com.example.onigiri_restaurant_recommendation.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.onigiri_restaurant_recommendation.data.remote.network.Firebase
import com.example.onigiri_restaurant_recommendation.databinding.ActivitySignUpBinding
import com.example.onigiri_restaurant_recommendation.model.TokenFcm
import com.example.onigiri_restaurant_recommendation.ui.auth.login.LoginActivity
import com.example.onigiri_restaurant_recommendation.ui.main.MainActivity
import com.example.onigiri_restaurant_recommendation.ui.notification.MessagingService
import com.google.firebase.auth.ktx.userProfileChangeRequest

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

            Firebase.authInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this@SignUpActivity) { task ->
                    if (task.isSuccessful) {
                        val token = MessagingService().getToken()

                        Firebase.firestoreInstance()
                            .collection("token_fcm")
                            .document(Firebase.currentUser().uid)
                            .set(TokenFcm(token, Firebase.currentUser().uid))
                            .addOnSuccessListener {
                                Log.d(TAG, "Saved token $token")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error", e)
                            }

                        Firebase.currentUser().updateProfile(
                            userProfileChangeRequest {
                                displayName = name
                            }
                        ).addOnCompleteListener {
                            if (task.isSuccessful) {
                                Log.d("profileChangeRequest", "User profile updated.")
                            }
                        }

                        startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@SignUpActivity, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}