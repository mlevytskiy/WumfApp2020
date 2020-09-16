package com.core.dynamicfeature.di

import android.content.Context
import android.util.Log
import com.core.wumfapp2020.di.AppComponent
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import krafts.alex.tg.TgClient
import krafts.alex.tg.TgModule
import javax.inject.Scope
import javax.inject.Singleton


@OnBoardingScope
@Component(
    dependencies = [AppComponent::class],
    modules = [OnBoardingAssistedModule::class]
)
interface OnBoardingComponent : ViewModelProvision

@Scope
annotation class  OnBoardingScope

@AssistedModule
@Module(includes = [AssistedInject_OnBoardingAssistedModule::class])
abstract class OnBoardingAssistedModule



