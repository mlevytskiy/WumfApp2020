package com.core.wumfapp2020.di

import android.content.Context
import com.core.wumfapp2020.memory.UserInfoRepository
import com.core.wumfapp2020.testdi.Obj1T
import com.core.wumfapp2020.viewmodel.HomeViewModel
import com.core.wumfapp2020.viewmodel.MainActivityViewModel
import com.core.wumfapp2020.viewmodel.PreOnBoardingViewModel
import com.core.wumfapp2020.viewmodel.SharedViewModel
import com.google.android.play.core.splitinstall.SplitInstallManager

interface ViewModelProvision {
    var preOnBoardingViewModel: PreOnBoardingViewModel
    var homeViewModel: HomeViewModel
    var mainActivityViewModel: MainActivityViewModel
    var sharedViewModel: SharedViewModel
}

interface ForDeliveryFeaturesProvision {
    fun provideContext(): Context
    fun provideCustomObject(): Obj1T
    fun provideSplitInstallManager(): SplitInstallManager
    fun provideUserInfoRepository(): UserInfoRepository
}