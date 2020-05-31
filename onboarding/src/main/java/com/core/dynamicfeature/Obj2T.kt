package com.core.dynamicfeature

import android.content.Context
import android.util.Log
import com.core.dynamicfeature.di.OnBoardingScope
import javax.inject.Inject

@OnBoardingScope
class Obj2T @Inject constructor(val context: Context) {

    fun test() {
        Log.i("testr", "Obj2T test()" + context)
    }
}