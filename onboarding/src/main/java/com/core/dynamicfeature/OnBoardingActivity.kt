package com.core.dynamicfeature

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.core.wumfapp2020.vo.FailedOnBoardingResult
import com.core.wumfapp2020.vo.FailedResultReason
import com.core.wumfapp2020.vo.SuccessOnBoardingResult
import com.google.android.play.core.splitcompat.SplitCompat

const val ON_BOARDING_RESULT_KEY = "ON_BOARDING_RESULT_KEY"

class OnBoardingActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bord)
        setCanceledResult()
        setSuccessResult()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }

    private fun setCanceledResult() {
        val intent = Intent()
        intent.putExtra(ON_BOARDING_RESULT_KEY, FailedOnBoardingResult(FailedResultReason.CANCELED_BY_USER))
        setResult(RESULT_CANCELED, intent)
    }

    private fun setSuccessResult() {
        val intent = Intent()
        intent.putExtra(ON_BOARDING_RESULT_KEY, SuccessOnBoardingResult(1111))
        setResult(RESULT_OK, intent)
    }
}