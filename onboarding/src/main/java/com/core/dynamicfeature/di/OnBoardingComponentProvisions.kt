package com.core.dynamicfeature.di

import com.core.dynamicfeature.viewmodel.OnBoardingViewModel

interface ViewModelProvision {
    var onBoardingViewModelFactory: OnBoardingViewModel.Factory
}