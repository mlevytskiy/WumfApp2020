package com.core.wumfapp2020.di

import android.app.Application
import android.content.Context
import com.core.wumfapp2020.InternetConnectionChecker
import com.core.wumfapp2020.testdi.WumfActivity
import javax.inject.Singleton
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import dagger.*

@Singleton
@Component(
    modules = [
        AppModule::class, AssistedModule::class
    ]
)
interface AppComponent: ViewModelProvision, ForDeliveryFeaturesProvision {

    fun inject(activity: WumfActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Application): AppComponent
    }

}

@com.squareup.inject.assisted.dagger2.AssistedModule
@Module(includes = [AssistedInject_AssistedModule::class])
abstract class AssistedModule

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

    @JvmStatic
    @Singleton
    @Provides
    fun provideInternetConnectionChecker(context: Context): InternetConnectionChecker {
        return InternetConnectionChecker(context)
    }

}
