package com.core.dynamicfeature.di

import androidx.fragment.app.Fragment
import com.core.dynamicfeature.viewmodel.DetectingYourPhoneNumberViewModel
import com.core.dynamicfeature.viewmodel.EnterPhoneNumberViewModel2
import com.core.dynamicfeature.viewmodel.EnterTelegramPasswordViewModel
import com.core.wumfapp2020.di.injector

interface ViewModelProvision {
    var detectingViewModelFactory: DetectingYourPhoneNumberViewModel.Factory
    var enterPhoneNumberViewModelFactory: EnterPhoneNumberViewModel2.Factory
    var enterTelegramPasswordViewModelFactory: EnterTelegramPasswordViewModel.Factory

}

val Fragment.featureInjector get() =  DaggerOnBoardingComponent.builder().appComponent(injector).build()

