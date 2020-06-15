package com.library.core

import android.content.Intent

interface ActivityResultHandler {

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    )

}