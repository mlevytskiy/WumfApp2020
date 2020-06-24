package com.core.dynamicfeature.di

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.app.api.api.HeaderInterceptor
import com.app.api.api.WumfApi
import com.core.dynamicfeature.BuildConfig
import com.core.wumfapp2020.di.AppComponent
import com.library.telegramkotlinapi.handler.CommonHandler
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.drinkless.td.libcore.telegram.Client
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Scope
import javax.inject.Singleton


@OnBoardingScope
@Component(
    dependencies = [AppComponent::class],
    modules = [OnBoardingAssistedModule::class, RetrofitModule::class]
)
interface OnBoardingComponent : ViewModelProvision

@Scope
annotation class  OnBoardingScope

@AssistedModule
@Module(includes = [AssistedInject_OnBoardingAssistedModule::class])
abstract class OnBoardingAssistedModule

private const val API_KEY_HEADER_KEY = "x-api-key"
private const val DEVICE_ID_HEADER_KEY = "device-id"
private const val APP_VERSION_HEADER_KEY = "AppVersion"
private const val TRACE_ID_HEADER_KEY = "trace-id"

@Module
class RetrofitModule {

    private val BASE_URL = "https://radiant-plains-90522.herokuapp.com/"
//    private val BASE_URL = "http://192.168.0.105:8080/"

    @OnBoardingScope
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

    @OnBoardingScope
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

    @Reusable
    @Provides
    fun getTelegramClient(context: Context): Client {
        val handler = CommonHandler(context)
        val client = Client.create(handler,
            Client.ExceptionHandler { e -> Log.i("testr", "onException " + e) },
            Client.ExceptionHandler { e -> Log.i("testr", "onException " + e) })
        handler.client = client
        return client
    }

}

