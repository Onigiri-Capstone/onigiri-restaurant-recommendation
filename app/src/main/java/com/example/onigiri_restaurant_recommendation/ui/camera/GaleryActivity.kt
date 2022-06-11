package com.example.onigiri_restaurant_recommendation.ui.camera

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.model.BitmapClass


class GaleryActivity : AppCompatActivity() {
    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_galery)
        startGallery()
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri


            val file = getRealPathFromURI(this@GaleryActivity, selectedImg)
            Log.e(":file ", file.toString())
            //data image
            bitmap =
                BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImg))
            bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val intent =Intent(this@GaleryActivity,CameraPreviewActivity::class.java)
            intent.putExtra(CameraPreviewActivity.EXTRA_BITMAP,BitmapClass(bitmap))
            startActivity(intent)

        }
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val columnindex: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnindex)
        } finally {
            cursor?.close()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }
}