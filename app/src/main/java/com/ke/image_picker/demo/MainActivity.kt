package com.ke.image_picker.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.ke.image_picker.demo.databinding.ActivityMainBinding
import com.ke.module.image_picker.ImageSource
import com.ke.module.image_picker.KeImagePicker
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.callback.setOnClickListener {
            AlertDialog.Builder(this)
                .setSingleChoiceItems(arrayOf("相机", "相册"), 0) { dialog, which ->
                    dialog.dismiss()
                    val imageSource = if (which == 0) ImageSource.Camera else ImageSource.Gallery
                    KeImagePicker(supportFragmentManager)
                        .pickImage(imageSource) {
                            binding.image.setImageURI(it)
                        }
                }.show()
        }

        binding.coroutine.setOnClickListener {
            AlertDialog.Builder(this)
                .setSingleChoiceItems(arrayOf("相机", "相册"), 0) { dialog, which ->
                    dialog.dismiss()

                    lifecycleScope.launch {
                        val imageSource =
                            if (which == 0) ImageSource.Camera else ImageSource.Gallery
                        val uri = KeImagePicker(supportFragmentManager)
                            .pickImage(imageSource)
                        binding.image.setImageURI(uri)
                    }


                }.show()
        }
    }
}