package com.ke.module.image_picker

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImagePickerFragment : Fragment() {

    var resultHandler: ((Uri?) -> Unit)? = null

    private lateinit var gallerylauncher: ActivityResultLauncher<String>

    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>

    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>

    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        gallerylauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            resultHandler?.invoke(it)
        }

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                resultHandler?.invoke(photoUri)
            } else {
                resultHandler?.invoke(null)
            }
        }

        requestCameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    val file = createCameraImageFile()
                    photoUri = FileProvider.getUriForFile(
                        requireContext(),
                        requireActivity().packageName + ".image_picker.provider",
                        file
                    )
                    cameraLauncher.launch(photoUri)
                } else {
                    resultHandler?.invoke(null)
                }
            }

    }

    @SuppressLint("SimpleDateFormat")
    private fun createCameraImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
    }

    internal fun pick(imageSource: ImageSource) {
        if (imageSource == ImageSource.Camera) {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            gallerylauncher.launch("image/*")
        }
    }
}