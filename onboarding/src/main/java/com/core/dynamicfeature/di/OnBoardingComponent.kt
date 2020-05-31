package com.core.dynamicfeature.di

import android.app.Application
import android.content.Context
import com.core.dynamicfeature.OnBoardingActivity2
import com.core.wumfapp2020.DynamicApp
import com.core.wumfapp2020.di.AppComponent2
import com.core.wumfapp2020.di.AppModule
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope
import javax.inject.Singleton

@OnBoardingScope
@Component(
    dependencies = [AppComponent2::class]
)
interface OnBoardingComponent {

    fun inject(activity: OnBoardingActivity2)

}

@Scope
annotation class  OnBoardingScope

