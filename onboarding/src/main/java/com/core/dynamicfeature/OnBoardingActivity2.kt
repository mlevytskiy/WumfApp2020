package com.core.dynamicfeature

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.core.dynamicfeature.di.DaggerOnBoardingComponent
import com.core.dynamicfeature.di.OnBoardingComponent
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.testdi.Obj1T
import com.google.android.play.core.splitcompat.SplitCompat
import javax.inject.Inject

class OnBoardingActivity2: AppCompatActivity() {

    private lateinit var onBoardingInjector: OnBoardingComponent

    @Inject
    lateinit var obj2T: Obj2T
    @Inject
    lateinit var obj1T: Obj1T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bord)
        onBoardingInjector = DaggerOnBoardingComponent.builder().appComponent2(injector).build()
        onBoardingInjector.inject(this)
        obj2T.test()
        obj1T.test()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}