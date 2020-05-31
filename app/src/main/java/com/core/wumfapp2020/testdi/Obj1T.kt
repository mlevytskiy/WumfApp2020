package com.core.wumfapp2020.testdi

import android.content.Context
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Obj1T @Inject constructor(val context: Context) {

    fun test() {
        Log.i("testr", "test()" + context)
    }
}