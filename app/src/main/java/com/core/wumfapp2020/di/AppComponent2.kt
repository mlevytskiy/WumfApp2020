package com.core.wumfapp2020.di

import android.app.Application
import android.content.Context
import com.core.wumfapp2020.testdi.Obj1T
import com.core.wumfapp2020.testdi.WumfActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory

@Singleton
@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent2 {

    fun provideContext(): Context
    fun provideCustomObject(): Obj1T
    fun provideSplitInstallManager(): SplitInstallManager

    fun inject(activity: WumfActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Application): AppComponent2
    }

}

@Module
object AppModule {

    @JvmStatic
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @JvmStatic
    @Singleton
    @Provides
    fun provideSplitInstallManager(application: Application): SplitInstallManager {
        return SplitInstallManagerFactory.create(application)
    }

}