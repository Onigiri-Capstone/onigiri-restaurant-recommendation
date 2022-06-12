package com.example.onigiri_restaurant_recommendation.ui.camera

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.databinding.ActivityCameraPreviewBinding
import com.example.onigiri_restaurant_recommendation.ml.Model1654305306
import com.example.onigiri_restaurant_recommendation.model.BitmapClass
import com.example.onigiri_restaurant_recommendation.ui.result.ResultActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File

class CameraPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraPreviewBinding
    private lateinit var picture: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myFile = intent.getSerializableExtra("picture") as File?
        val myFILE = intent.getParcelableExtra<BitmapClass>(EXTRA_BITMAP) as BitmapClass?
        if (myFile != null) {
            picture = rotateBitmap(BitmapFactory.decodeFile(myFile.path))
        }
        else if(myFILE != null){
            picture = rotateBitmap(myFILE.bitmap)
        }


            binding.ivFood.setImageBitmap(picture)

        binding.btnCancel.setOnClickListener {
            startActivity(Intent(this@CameraPreviewActivity, CameraActivity::class.java))
        }

        progresbar(binding.progresbarhorizontal, binding.tvProgress, picture)
    }

    private fun progresbar(progresbar: ProgressBar, tvProgress: TextView, picture: Bitmap) {
        var foodname = ""
        lifecycleScope.launch(Dispatchers.Default) {
            //simulate process in background thread
            for (i in 1..10) {
                delay(200)
                val percentage = i * 10
                withContext(Dispatchers.Main) {
                    //update ui in main thread
                    tvProgress.text = String.format(getString(R.string.TextPersen), percentage)

                    if (percentage == 30) {
                        foodname = outputGeneratoronigiri(picture)
                    }
                }
            }

            val intent = Intent(this@CameraPreviewActivity, ResultActivity::class.java)
            intent.putExtra(ResultActivity.FOOD_NAME, foodname)
            startActivity(intent)
            finish()
        }
        progresbar.max = 10
        val currentProgresbar = 10
        ObjectAnimator.ofInt(progresbar, "progress", currentProgresbar)
            .setDuration(2000)
            .start()

    }

    private fun outputGeneratoronigiri(bitmap: Bitmap): String {
        val name_file = "labels.txt"
        val label = application.assets.open(name_file).bufferedReader().use { it.readText() }
        val labels = label.split("\n")
        val model = Model1654305306.newInstance(this)
        val bitmapscale = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
//        imageView.setImageBitmap(bitmapscale)
        // Creates inputs for reference.
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmapscale)

        val byteBuffer = tensorImage.buffer
        Log.d("shape", byteBuffer.toString())
        Log.d("shape", inputFeature0.buffer.toString())
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val max = getMax(outputFeature0.floatArray, outputFeature0.floatArray.size)
        Log.e("outputGenerator: ", "-----------------------")
        Log.e("outputGenerator: ", outputFeature0.floatArray.toList().toString())
        Log.e("outputGenerator: ", max.toString())
        Log.e("outputGenerator: ", outputFeature0.floatArray.size.toString())
        Log.e("outputGenerator: ", outputFeature0.dataType.toString())
        Log.e("outputGenerator: ", labels[max])
//        tvOutput.text = labels[max]
        model.close()

        return labels[max]
    }

    fun getMax(arr: FloatArray, size: Int): Int {
        var ind = 0
        var min = 0.0f

        for (i in 0 until size) {

            if (arr[i] > min) {
                Log.e("getMax: ", i.toString())
                Log.e("getMax: ", arr[i].toString())

                min = arr[i]
                ind = i
            }
        }
        return ind
    }

    fun rotateBitmap(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(90f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    companion object {
        const val EXTRA_BITMAP = ""
    }
}