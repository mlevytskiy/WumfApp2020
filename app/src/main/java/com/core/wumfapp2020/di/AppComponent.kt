package com.core.wumfapp2020.di

import android.app.Application
import android.content.Context
import android.provider.Settings
import com.app.api.api.HeaderInterceptor
import com.app.api.api.WumfApi
import com.core.wumfapp2020.BuildConfig
import com.core.wumfapp2020.InternetConnectionChecker
import com.core.wumfapp2020.base.ColorRes
import com.core.wumfapp2020.base.StringRes
import com.core.wumfapp2020.base.countriesdialog.CountriesHolder
import com.core.wumfapp2020.memory.HomeStateRepository
import com.core.wumfapp2020.memory.MyAppsCollectionRepository
import com.core.wumfapp2020.memory.MyObjectBox
import com.core.wumfapp2020.memory.UserInfoRepository
import com.core.wumfapp2020.testdi.WumfActivity
import javax.inject.Singleton
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import dagger.*
import io.objectbox.BoxStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val API_KEY_HEADER_KEY = "x-api-key"
private const val DEVICE_ID_HEADER_KEY = "device-id"
private const val APP_VERSION_HEADER_KEY = "AppVersion"
private const val TRACE_ID_HEADER_KEY = "trace-id"

@Singleton
@Component(
    modules = [
        AppModule::class, AssistedModule::class, RetrofitModule::class
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

    @JvmStatic
    @Singleton
    @Provides
    fun provideUserInfoRepository(boxStore: BoxStore): UserInfoRepository {
        val repository = UserInfoRepository(boxStore)
        repository.initCache()
        return repository
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideHomeStateRepository(boxStore: BoxStore): HomeStateRepository {
        val repository = HomeStateRepository(boxStore)
        repository.initCache()
        return repository
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideMyAppsCollectionRepository(boxStore: BoxStore): MyAppsCollectionRepository {
        val repository = MyAppsCollectionRepository(boxStore)
        repository.initCache()
        return repository
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideBoxStore(context: Context): BoxStore {
        return MyObjectBox.builder().androidContext(context).buildDefault()
    }

    @JvmStatic
    @Reusable
    @Provides
    fun provideCountryHolder(context: Context): CountriesHolder {
        return CountriesHolder(context)
    }

    @JvmStatic
    @Reusable
    @Provides
    fun provideStringRes(context: Context) = StringRes(context.resources)

    @JvmStatic
    @Reusable
    @Provides
    fun provideColorRes(context: Context) = ColorRes(context)

}

@Module
class RetrofitModule {

    private val BASE_URL = "https://radiant-plains-90522.herokuapp.com/"
//    private val BASE_URL = "http://192.168.0.105:8080/"

    @Reusable
    @Provides
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideOkHttpClient(headers: HeaderInterceptor): OkHttpClient {
        val loginInterceptor= HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(headers)
        builder.addInterceptor(loginInterceptor)
        return builder.build()
    }

    @Reusable
    @Provides
    fun provideHeaderInterceptor(context: Context): HeaderInterceptor {
        return HeaderInterceptor(HashMap(mapOf(
            DEVICE_ID_HEADER_KEY to Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            ),
            APP_VERSION_HEADER_KEY to "Android/${BuildConfig.VERSION_CODE}/${BuildConfig.VERSION_NAME}"
        )))
    }

    @Reusable
    @Provides
    fun getWumfApi(retrofit: Retrofit): WumfApi {
        return retrofit.create(WumfApi::class.java)
    }
}
