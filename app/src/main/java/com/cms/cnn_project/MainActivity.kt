package com.cms.cnn_project

import android.app.ComponentCaller
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cms.cnn_project.adapter.FoodAdapter
import com.cms.cnn_project.databinding.ActivityMainBinding
import com.cms.cnn_project.entity.Food
import com.cms.cnn_project.ml.Model
import com.cms.cnn_project.viewModel.FoodViewModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var foodViewModel: FoodViewModel

    private val manager = supportFragmentManager
    private lateinit var labels: List<String>
    private var bitmap: Bitmap?= null
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            try {
                val imageUrl = uri.toString()
                val inputStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)

                analyzeImageAndSaveToDB(imageUrl, bitmap!!)
            } catch (e: Exception) {
                Log.e("error", "Failed to process image: ${e.message}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initApp()
        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]

        binding.mainAddPhotoBtn.setOnClickListener{
            getContent.launch("image/*")
        }
    }

    private fun initApp() {
        changeFragment(FoodFragment())
        initBottomNav()
        labels = application.assets.open("labels.txt").bufferedReader().readLines()
    }

    private fun initBottomNav() {
        binding.mainBottomNav.menu.getItem(1).isEnabled = false

        binding.mainBottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> {
                    changeFragment(FoodFragment())
                }

                R.id.nav_recipe -> {
                    changeFragment(RecipeFragment())
                }
            }
            return@setOnItemSelectedListener true
        }

        binding.mainBottomNav.setOnItemReselectedListener {  }
    }

    private fun changeFragment(fragment: Fragment) {
        val transaction = manager.beginTransaction()
            .replace(R.id.main_container, fragment)
        transaction.commitAllowingStateLoss()
    }

    private fun analyzeImageAndSaveToDB(imageUrl: String ,bitmap: Bitmap) {
        // 1. Bitmap 크기 조정
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 64, 64, true)

        // 2. TensorFlow Lite 전처리
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(64, 64, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0.0f, 255.0f))
            .build()

        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(resizedBitmap)
        val processedImage = imageProcessor.process(tensorImage)

        // 3. TensorFlow Lite 모델 예측
        val model = Model.newInstance(this)

        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 64, 64, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(processedImage.buffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
        model.close()

        // 4. 예측 결과 처리
        var maxIdx = 0
        outputFeature0.forEachIndexed { index, fl ->
            if (outputFeature0[maxIdx] < fl) {
                maxIdx = index
            }
        }
        val nameKo = labels[maxIdx]
        val nameEn = translateLabelToEnglish(nameKo)

        val foodEntity = Food(imageUrl, nameKo, nameEn)
        foodViewModel.insertImage(foodEntity)
    }


}