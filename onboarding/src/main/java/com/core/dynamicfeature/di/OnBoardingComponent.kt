package com.core.dynamicfeature.di

import com.core.wumfapp2020.di.AppComponent
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Component
import dagger.Module
import javax.inject.Scope


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

