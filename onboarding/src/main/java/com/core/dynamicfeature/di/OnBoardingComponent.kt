package com.core.dynamicfeature.di

import com.core.wumfapp2020.di.AppComponent
import dagger.Component
import javax.inject.Scope

@OnBoardingScope
@Component(
    dependencies = [AppComponent::class]
)
interface OnBoardingComponent : ViewModelProvision

@Scope
annotation class  OnBoardingScope

