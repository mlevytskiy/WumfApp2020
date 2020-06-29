package com.core.dynamicfeature.di

import android.content.Context
import android.util.Log
import com.core.wumfapp2020.di.AppComponent
import com.library.telegramkotlinapi.handler.CommonHandler
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Reusable
import org.drinkless.td.libcore.telegram.Client
import javax.inject.Scope


@OnBoardingScope
@Component(
    dependencies = [AppComponent::class],
    modules = [OnBoardingAssistedModule::class, TelegramModule::class]
)
interface OnBoardingComponent : ViewModelProvision

@Scope
annotation class  OnBoardingScope

@AssistedModule
@Module(includes = [AssistedInject_OnBoardingAssistedModule::class])
abstract class OnBoardingAssistedModule

@Module
class TelegramModule {
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



