package com.library.core

import android.content.Intent

interface OnActivityResultHandler {

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    )
}