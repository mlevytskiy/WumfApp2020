package com.core.dynamicfeature.fragment

import androidx.databinding.ViewDataBinding
import com.core.dynamicfeature.databinding.FrgOnboardingBinding
import com.core.dynamicfeature.di.DaggerOnBoardingComponent
import com.core.dynamicfeature.viewmodel.OnBoardingViewModel
import com.core.wumfapp2020.di.injector
import com.library.core.BaseFragment
import com.library.core.BaseViewModel
import com.library.core.di.unsyncLazy

abstract class AnyOnBoardingFragment<B : ViewDataBinding, VM : BaseViewModel>: BaseFragment<FrgOnboardingBinding, OnBoardingViewModel>() {

    protected val featureInjector by unsyncLazy { DaggerOnBoardingComponent.builder().appComponent(injector).build() }

}