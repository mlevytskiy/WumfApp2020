package com.core.dynamicfeature.di

import androidx.fragment.app.Fragment
import com.core.dynamicfeature.viewmodel.OnBoardingViewModel
import com.core.wumfapp2020.di.injector

interface ViewModelProvision {
    var onBoardingViewModelFactory: OnBoardingViewModel.Factory
}

val Fragment.featureInjector get() =  DaggerOnBoardingComponent.builder().appComponent(injector).build()