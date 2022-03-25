package com.fujigames.getresultstest

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.fujigames.getresultstest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnImage.setOnClickListener {
            openSomeActivityForResult()
        }
        binding.btnNormal.setOnClickListener {
            val buttons = binding.previewIV.result
            binding.ivOutput.setImageBitmap(buttons[0])
        }
    }

    fun openSomeActivityForResult() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(gallery)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data = result.data?.data
                data?.let {
                    applicationContext?.let {

                        if (Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                it.contentResolver,
                                data
                            )
                            binding.previewIV.addImage(bitmap)
                        } else {
                            val source = ImageDecoder.createSource(it.contentResolver, data)
                            val bitmap = ImageDecoder.decodeBitmap(source)

                            binding.previewIV.addImage(bitmap)
                        }

                    }

                }
            }


        }
}