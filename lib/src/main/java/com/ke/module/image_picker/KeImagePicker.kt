package com.ke.module.image_picker

import android.net.Uri
import androidx.fragment.app.FragmentManager
import kotlin.coroutines.suspendCoroutine

class KeImagePicker(private val fragmentManager: FragmentManager) {


    fun pickImage(
        imageSource: ImageSource,
        callback: (Uri?) -> Unit
    ) {
        getFragment().resultHandler = callback
        getFragment().pick(imageSource)
    }

    suspend fun pickImage(imageSource: ImageSource): Uri? {

        val fragment = getFragment()
        return suspendCoroutine {
            fragment.resultHandler = { uri ->
                it.resumeWith(Result.success(uri))
            }
            fragment.pick(imageSource)
        }
    }

    private fun getFragment(): ImagePickerFragment {
        var fragment = fragmentManager.findFragmentByTag(TAG) as? ImagePickerFragment

        if (fragment == null) {
            fragment = ImagePickerFragment()
            fragmentManager.beginTransaction().add(fragment, TAG).commitNow()
        }

        return fragment
    }

    companion object {
        private val TAG = ImagePickerFragment::class.java.name
    }
}