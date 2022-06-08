package com.example.onigiri_restaurant_recommendation.ui.detailrestaurant

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.databinding.ActivityDetailRestaurantBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class DetailRestaurantActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailRestaurantBinding
    private val detailRestaurantViewModel: DetailRestaurantViewModel by viewModels()
    private lateinit var placeId: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lon: Double = 0.0
    private var lat: Double = 0.0

    private var lonRestaurant: Double = 0.0
    private var phonenumberRestaurant: String = ""
    private var latRestaurant: Double = 0.0
    private var dataRestaurant : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        placeId = intent.getStringExtra(PLACE_ID)!!
        Log.e("onCreate: ", placeId)
        detailRestaurantViewModel.setDetailRestaurant(placeId, lat, lon)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setToolbar()
        setDetailRestaurant()
        getMyLastLocation()
    }

    private fun setDetailRestaurant() {
        detailRestaurantViewModel.getDetailRestaurant().observe(this) {
            binding.apply {
                latRestaurant = it.geometry.location.lat
                lonRestaurant = it.geometry.location.lng
                Log.e("setDetailRestaurant: ", it.toString())
                dataRestaurant = "${it.name} ${it.formatted_address} ${it.url} "
                tvName.text = it.name
                rateRestaurant.text = it.rating.toString()
                idTvRatingBar.rating = it.rating
                vicinity.text = it.formatted_address
                vicinity.setOnClickListener(this@DetailRestaurantActivity)
                pluscode.text = it.plus_code.compound_code
                pluscode.setOnClickListener(this@DetailRestaurantActivity)
                phonenumber.text =it.formatted_phone_number
                phonenumberRestaurant = it.formatted_phone_number
                layooutphone.setOnClickListener(this@DetailRestaurantActivity)
                operatingHour.text = opratioanlhour(it.opening_hours.weekday_text)
                direction.setOnClickListener(this@DetailRestaurantActivity)
                share.setOnClickListener(this@DetailRestaurantActivity)
                favorite.setOnClickListener(this@DetailRestaurantActivity)
                phoneCall.setOnClickListener(this@DetailRestaurantActivity)
                nestedlist.visibility= View.VISIBLE
            }
        }
    }

    private fun opratioanlhour(weekdayText: List<String>?): String {
        var str = "Operating Hour\n"
        if (weekdayText != null) {
            for (element in weekdayText)
                str = "$str \n${element}"
        }
        return str
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolBarLayout)
        supportActionBar?.apply {

            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLastLocation()
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lon = location.longitude
                    lat = location.latitude
                    Log.e("getMyLastLocation: ", "$lon $lat")
                } else {
                    Toast.makeText(
                        this@DetailRestaurantActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    companion object {

        const val PLACE_ID = "place_id"
    }

    override fun onClick(v: View?) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        when(v?.id){
            R.id.vicinity -> {
                val clipData = ClipData.newPlainText("text", binding.vicinity.text)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(this, "Address copied to clipboard", Toast.LENGTH_LONG).show()
            }
            R.id.pluscode -> {
                val clipData = ClipData.newPlainText("text", binding.pluscode.text)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(this, "Plus Code copied to clipboard", Toast.LENGTH_LONG).show()
            }
            R.id.layooutphone -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phonenumberRestaurant")
                startActivity(intent)
            }
            R.id.phone_call ->{
                val intent = Intent(Intent.ACTION_DIAL)

                intent.data = Uri.parse("tel:$phonenumberRestaurant")
                startActivity(intent)
            }
            R.id.direction -> {
                val uri = Uri.parse("http://maps.google.com/maps?saddr=$lat,$lon &daddr=$latRestaurant,${lonRestaurant}Restaurant &dirflg=d")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            R.id.share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, dataRestaurant)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share Via"))
            }
        }
    }
}