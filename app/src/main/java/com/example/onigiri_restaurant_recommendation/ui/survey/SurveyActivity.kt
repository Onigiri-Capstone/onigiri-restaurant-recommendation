package com.example.onigiri_restaurant_recommendation.ui.survey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.data.remote.network.Firebase
import com.example.onigiri_restaurant_recommendation.databinding.ActivitySurveyBinding
import com.example.onigiri_restaurant_recommendation.model.FavoriteFood
import com.example.onigiri_restaurant_recommendation.model.TokenFcm
import com.example.onigiri_restaurant_recommendation.model.User
import com.example.onigiri_restaurant_recommendation.ui.auth.signup.SignUpActivity
import com.example.onigiri_restaurant_recommendation.ui.main.MainActivity
import com.example.onigiri_restaurant_recommendation.ui.notification.MessagingService
import com.google.firebase.auth.ktx.userProfileChangeRequest

class SurveyActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySurveyBinding
    private lateinit var checkbox: CheckBox
    private var checkString = arrayListOf<String>()
    private var checkId = arrayListOf<Int>()
    private lateinit var user: User

    companion object {
        private const val TAG = "SurveyActivity"
        const val DATA_USER = "data_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra<User>(DATA_USER) as User

        setCheckBox()

        binding.btnSubmit.setOnClickListener {
            submitData()
        }
    }

    private fun submitData() {
        if(checkString.size == 3){
            binding.loading.isVisible = true
            Firebase.authInstance().createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener(this@SurveyActivity) { task ->
                    if (task.isSuccessful) {
                        addTokenFcm()
                        updateUserProfile()
                        addUserFavoriteFood(checkString[0], checkString[1], checkString[2])

                        Firebase.firebaseMessaging().subscribeToTopic("onigiri")
                            .addOnCompleteListener { task ->
                                var msg = "Subscribed"
                                if (!task.isSuccessful) {
                                    msg = "Subscribe failed"
                                }
                                Log.d(TAG, msg)
                            }

                        binding.loading.isVisible = false
                        startActivity(Intent(this@SurveyActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@SurveyActivity, "Failed", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SurveyActivity, SignUpActivity::class.java))
                        finish()
                    }
                }
        } else {
            Toast.makeText(this, "Choose three and submit.", Toast.LENGTH_SHORT).show()
        }


    }

    private fun addTokenFcm() {
        val token = MessagingService().getToken()

        if(token != "" || !token.isNullOrEmpty()) {
            Firebase.firestoreInstance()
                .collection("token_fcm")
                .add(
                    hashMapOf(
                        "token" to token
                    )
                )
                .addOnSuccessListener {
                    Log.d(TAG, "Saved token $token")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error", e)
                }
        }
    }

    private fun updateUserProfile() {
        Firebase.currentUser().updateProfile(
            userProfileChangeRequest {
                displayName = user.name
            }
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("profileChangeRequest", "User profile updated.")
            }
        }
    }

    private fun addUserFavoriteFood(first: String, second: String, third: String) {
        Firebase.firestoreInstance()
            .collection("user_favorite")
            .document(Firebase.currentUser().uid)
            .set(FavoriteFood(first, second, third))
            .addOnSuccessListener {
                Log.d(TAG, "addUserFavoriteFood: Data added")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error addUserFavoriteFood()", e)
            }
    }

    private fun setCheckBox() {
        binding.apply {
            idSweets.setOnClickListener {
                checkData(R.id.id_sweets, idSweets.text, idSweets.isChecked)
            }
            idRice.setOnClickListener {
                checkData(R.id.id_rice, idRice.text, idRice.isChecked)
            }
            idMeatball.setOnClickListener {
                checkData(R.id.id_meatball, idMeatball.text, idMeatball.isChecked)
            }
            idChicken.setOnClickListener {
                checkData(R.id.id_chicken, idChicken.text, idChicken.isChecked)
            }
            idDrinks.setOnClickListener {
                checkData(R.id.id_drinks, idDrinks.text, idDrinks.isChecked)
            }
            idCoffee.setOnClickListener {
                checkData(R.id.id_coffee, idCoffee.text, idCoffee.isChecked)
            }
            idSeafood.setOnClickListener {
                checkData(R.id.id_seafood, idSeafood.text, idSeafood.isChecked)
            }
            idWestern.setOnClickListener {
                checkData(R.id.id_western, idWestern.text, idWestern.isChecked)
            }
            idNoodle.setOnClickListener {
                checkData(R.id.id_noodle, idNoodle.text, idNoodle.isChecked)
            }
            idChinese.setOnClickListener {
                checkData(R.id.id_chinese, idChinese.text, idChinese.isChecked)
            }
            idIndian.setOnClickListener {
                checkData(R.id.id_indian, idIndian.text, idIndian.isChecked)
            }
            idJapanese.setOnClickListener {
                checkData(R.id.id_japanese, idJapanese.text, idJapanese.isChecked)
            }
            idMiddleEast.setOnClickListener {
                checkData(R.id.id_middle_east, idMiddleEast.text, idMiddleEast.isChecked)
            }
            idThai.setOnClickListener {
                checkData(R.id.id_thai, idThai.text, idThai.isChecked)
            }
        }
    }

    private fun checkData(id: Int, text: CharSequence, checked: Boolean) {
        if (checked) {
            if (!checkId.contains(id)) {
                if (checkString.size <= 2) {
                    Log.e("checkData: ", checkString.size.toString())
                    checkString.add(text.toString())
                    checkId.add(id)
                    Log.e("checkData: ", checkString.toString())
                } else {
                    val indeks = checkId[0]
                    checkbox = findViewById(indeks)
                    checkbox.isChecked = false
                    Log.e("checkData: ", checkString[0])
                    checkString.removeFirst()
                    checkId.removeFirst()

                    checkString.add(text.toString())
                    checkId.add(id)
                    Log.e("else: ", checkString.toString())
                }
            }
        } else {
            checkString.remove(text)
            checkId.remove(id)
            Log.e("else: ", checkString.toString())
        }
    }
}
