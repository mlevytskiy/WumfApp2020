//package com.core.dynamicfeature.di
//
//import android.app.Application
//import android.content.Context
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.core.wumfapp2020.DynamicApp
//import com.core.wumfapp2020.MainActivity
//import com.core.wumfapp2020.fragment.PreOnBoardingFragment
//import com.core.wumfapp2020.fragment.SampleFragment1
//import com.core.wumfapp2020.fragment.SampleFragment2
//import com.core.wumfapp2020.viewmodel.PreOnBoardingViewModule
//import com.core.wumfapp2020.viewmodel.SampleModule1
//import com.core.wumfapp2020.viewmodel.SampleModule2
//import com.google.android.play.core.splitinstall.SplitInstallManager
//import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
//import dagger.BindsInstance
//import dagger.android.support.AndroidSupportInjectionModule
//import dagger.Component
//import dagger.Module
//import dagger.Provides
//import dagger.android.AndroidInjector
//import javax.inject.Provider
//import javax.inject.Singleton
//
//@Singleton
//@Component(
//    modules = [
//        AndroidSupportInjectionModule::class,
//        AppModule::class,
//        ActivityModule::class,
//        FragmentsModule::class
//    ]
//)
//interface AppComponent : AndroidInjector<DynamicApp> {
//    @Component.Builder
//    interface Builder {
//        @BindsInstance
//        fun application(application: Application): Builder
//
//        fun build(): AppComponent
//    }
//}
//
//@Module
//class AppModule {
//
//    @Singleton
//    @Provides
//    fun provideContext(application: Application): Context = application.applicationContext
//
//    @Singleton
//    @Provides
//    fun provideSplitInstallManager(application: Application): SplitInstallManager {
//        return SplitInstallManagerFactory.create(application)
//    }
//
//    @Module
//    companion object {
//        @JvmStatic
//        @Provides
//        fun provideViewModelFactory(
//            providers: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
//        ): ViewModelProvider.Factory = ViewModelFactory(providers)
//    }
//}
//
//@Module
//abstract class ActivityModule {
//    @ContributesAndroidInjector
//    internal abstract fun contributeProductListActivity(): MainActivity
//}
//
//@Module
//abstract class FragmentsModule {
//
//    @ContributesAndroidInjector(modules = [SampleModule1::class])
//    abstract fun bindSample1(): SampleFragment1
//
//    @ContributesAndroidInjector(modules = [SampleModule2::class])
//    abstract fun bindSample2(): SampleFragment2
//
//    @ContributesAndroidInjector(modules = [PreOnBoardingViewModule::class])
//    abstract fun bindPreOnBoarding(): PreOnBoardingFragment
//
//}