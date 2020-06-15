package com.core.dynamicfeature.di

import androidx.fragment.app.Fragment
import com.core.dynamicfeature.viewmodel.DetectingYourPhoneNumberViewModel
import com.core.dynamicfeature.viewmodel.EnterPhoneNumberViewModel
import com.core.dynamicfeature.viewmodel.OnBoardingViewModel
import com.core.wumfapp2020.di.injector

interface ViewModelProvision {
    var onBoardingViewModelFactory: OnBoardingViewModel.Factory
    var detectingViewModelFactory: DetectingYourPhoneNumberViewModel.Factory
    var enterPhoneNumberViewModelFactory: EnterPhoneNumberViewModel.Factory
}

val Fragment.featureInjector get() =  DaggerOnBoardingComponent.builder().appComponent(injector).build()

