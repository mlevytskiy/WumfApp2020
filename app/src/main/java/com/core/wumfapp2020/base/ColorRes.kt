package com.core.wumfapp2020.base

import android.content.Context
import androidx.core.content.ContextCompat

class ColorRes(private val context: Context) {

    fun getColor(colorId: Int) = ContextCompat.getColor(context, colorId)

}