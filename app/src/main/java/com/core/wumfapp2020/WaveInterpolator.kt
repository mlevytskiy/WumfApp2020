package com.core.wumfapp2020

import android.view.animation.Interpolator

class WaveInterpolator : Interpolator {
    override fun getInterpolation(f: Float): Float {
        return if (f == 0.0f || f == 1.0f) f else Math.pow(
            2.0,
            (-10.0f * f).toDouble()
        ).toFloat() * Math.sin(((f - 0.120000005f) * 8.482301f / 0.6f).toDouble()).toFloat() + 1.0f
    }
}