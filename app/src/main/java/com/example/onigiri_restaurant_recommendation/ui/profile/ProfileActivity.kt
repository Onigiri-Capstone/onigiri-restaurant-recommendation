package com.example.onigiri_restaurant_recommendation.ui.profile

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.databinding.ActivityProfileBinding
import com.example.onigiri_restaurant_recommendation.databinding.DialogQuitBinding
import com.example.onigiri_restaurant_recommendation.remote.network.Firebase
import com.example.onigiri_restaurant_recommendation.ui.auth.login.LoginActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var bindingDialog: DialogQuitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setProfile()

        binding.menuLogout.setOnClickListener {
            bindingDialog = DialogQuitBinding.inflate(layoutInflater)
            val view = View.inflate(this@ProfileActivity, R.layout.dialog_quit, null)

            val builder = AlertDialog.Builder(this@ProfileActivity)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCancelable(false)

            val cancel = view.findViewById<Button>(R.id.btn_negative)
            cancel.setOnClickListener {
                dialog.dismiss()
            }

            val quit = view.findViewById<Button>(R.id.btn_positive)
            quit.setOnClickListener{
                Firebase.authInstance().signOut()
                startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
                finish()
            }

        }
        setToolbar()
    }

    private fun setProfile() {
        with(Firebase.currentUser()) {
            with(binding) {
                tvName.text = displayName
                tvEmail.text = email
            }
        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}