package com.core.wumfapp2020.di

import android.content.Context
import com.core.wumfapp2020.testdi.Obj1T
import com.core.wumfapp2020.viewmodel.MainActivityViewModel
import com.core.wumfapp2020.viewmodel.PreOnBoardingViewModel
import com.google.android.play.core.splitinstall.SplitInstallManager

interface ViewModelProvision {
    var preOnBoardingViewModel: PreOnBoardingViewModel
    var mainActivityViewModel: MainActivityViewModel
}

interface ForDeliveryFeaturesProvision {
    fun provideContext(): Context
    fun provideCustomObject(): Obj1T
    fun provideSplitInstallManager(): SplitInstallManager
}