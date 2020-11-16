package com.core.wumfapp2020.util

import android.content.Context
import com.core.wumfapp2020.R
import javax.inject.Inject

class Languages @Inject constructor(context: Context) {

    val all : List<String>

    init {
        all = context.resources.getStringArray(R.array.languages).asList()
    }

}