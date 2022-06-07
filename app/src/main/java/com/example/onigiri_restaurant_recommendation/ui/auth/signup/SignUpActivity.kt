package com.example.onigiri_restaurant_recommendation.ui.auth.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.onigiri_restaurant_recommendation.databinding.ActivitySignUpBinding
import com.example.onigiri_restaurant_recommendation.remote.network.Firebase
import com.example.onigiri_restaurant_recommendation.ui.auth.login.LoginActivity
import com.example.onigiri_restaurant_recommendation.ui.main.MainActivity
import com.example.onigiri_restaurant_recommendation.ui.notification.MessagingService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.userProfileChangeRequest

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

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
                        Firebase.currentUser().updateProfile(
                            userProfileChangeRequest {
                                displayName = name
                            }
                        ).addOnCompleteListener {
                            if (task.isSuccessful) {
                                Log.d("profileChangeRequest", "User profile updated.")
                            }

                            startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                            finish()
                        }

//                        val id = Firebase.currentUser().uid
//                        Firebase.fcmInstance().token.addOnCompleteListener ( OnCompleteListener {
//                            if (!it.isSuccessful) {
//                                Log.w(TAG, "Fetching FCM token failed", task.exception)
//                                return@OnCompleteListener
//                            }
//
//                            val id = Firebase.currentUser().uid
//                            Firebase.firestoreInstance()
//                                .document(id)
//                                .collection("token_fcm")
//                                .add(hashMapOf(
//                                    "token" to token
//                                ))
//                                .addOnSuccessListener {
//                                    Log.d(MessagingService.TAG, "Saved ID $id and token $token")
//                                }
//                                .addOnFailureListener { e ->
//                                    Log.w(MessagingService.TAG, "Error", e)
//                                }
//                        } )
                    }
                }
        }
    }
}