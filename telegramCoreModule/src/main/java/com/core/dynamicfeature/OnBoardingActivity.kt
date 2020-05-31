package com.core.dynamicfeature

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.android.play.core.splitcompat.SplitCompat

//com.core.dynamicfeature.OnBoardingActivity
class OnBoardingActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bord)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}