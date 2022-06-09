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
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.adapter.ViewPagerImageSlide
import com.example.onigiri_restaurant_recommendation.databinding.ActivityDetailRestaurantBinding
import com.example.onigiri_restaurant_recommendation.model.ImageModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.math.abs

class DetailRestaurantActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailRestaurantBinding
    private val detailRestaurantViewModel: DetailRestaurantViewModel by viewModels()
    private lateinit var placeId: String
    private lateinit var handler : Handler
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lon: Double = 0.0
    private var lat: Double = 0.0

    private lateinit var myModelArrayList: ArrayList<ImageModel>
    private var lonRestaurant: Double = 0.0
    private var phonenumberRestaurant: String = ""
    private var latRestaurant: Double = 0.0
    private var dataRestaurant: String = ""
    private lateinit var  viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handler = Handler(Looper.myLooper()!!)
        placeId = intent.getStringExtra(PLACE_ID)!!
        Log.e("onCreate: ", placeId)
        detailRestaurantViewModel.setDetailRestaurant(placeId, lat, lon)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        viewPager2 = binding.ViewPager2
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){})
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
                Glide.with(applicationContext)
                    .load(it.photo_url[0])
                    .into(imgRestaurant)

                rateRestaurant.text = it.rating.toString()
                idTvRatingBar.rating = it.rating
                if(it.formatted_address.isNotEmpty()){
                    layoutaddress.visibility = View.VISIBLE
                    vicinity.text = it?.formatted_address
                    vicinity.setOnClickListener(this@DetailRestaurantActivity)
                }

                layoutpluscode.visibility = View.VISIBLE
                pluscode.text = it?.plus_code?.compound_code
                pluscode.setOnClickListener(this@DetailRestaurantActivity)
                if (!it.opening_hours.weekday_text.isNullOrEmpty()){
                    layoutopratinghours.visibility = View.VISIBLE
                    operatingHour.text = opratioanlhour(it?.opening_hours?.weekday_text)
                }
                if(it.formatted_phone_number != ""){
                    layoutphone.visibility = View.VISIBLE
                    phonenumber.text = it?.formatted_phone_number
                    phonenumberRestaurant = it.formatted_phone_number
                }

                layooutphone.setOnClickListener(this@DetailRestaurantActivity)

                direction.setOnClickListener(this@DetailRestaurantActivity)
                share.setOnClickListener(this@DetailRestaurantActivity)
                favorite.setOnClickListener(this@DetailRestaurantActivity)
                phoneCall.setOnClickListener(this@DetailRestaurantActivity)
                setUpTransformer()

                myModelArrayList = ArrayList()
                for (posisition in 1 until it.photo_url.size){
                    myModelArrayList.add(ImageModel(it.photo_url[posisition]) )
                }
                Log.e("setDetailRestaurant: ", myModelArrayList.size.toString())

//                viewPagerAdapter = ViewPagerImageSlide(myModelArrayList, viewPager2)

                val viewPagerAdapter = ViewPagerImageSlide()
                viewPagerAdapter.setData(myModelArrayList)

                viewPager2.adapter = viewPagerAdapter
                viewPager2.offscreenPageLimit = 3
                viewPager2.clipToPadding = false
                viewPager2.clipChildren = false
                viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                nestedlist.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpTransformer(){
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
//            page.scaleY = 0.85f + r * 0.14f
        }

        viewPager2.setPageTransformer(transformer)
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
        when (v?.id) {
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
            R.id.phone_call -> {
                val intent = Intent(Intent.ACTION_DIAL)

                intent.data = Uri.parse("tel:$phonenumberRestaurant")
                startActivity(intent)
            }
            R.id.direction -> {
                val uri =
                    Uri.parse("http://maps.google.com/maps?saddr=$lat,$lon &daddr=$latRestaurant,${lonRestaurant}Restaurant &dirflg=d")
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