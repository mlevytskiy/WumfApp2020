package com.core.dynamicfeature.fragment

import com.core.dynamicfeature.R
import com.core.dynamicfeature.databinding.FrgOnboardingBinding
import com.core.dynamicfeature.di.DaggerOnBoardingComponent
import com.core.dynamicfeature.viewmodel.OnBoardingViewModel
import com.core.wumfapp2020.di.injector
import com.library.core.BaseFragment2
import com.library.core.di.lazyViewModel

class OnBoardingFragment: BaseFragment2<FrgOnboardingBinding, OnBoardingViewModel>() {

    val featureInjector by lazy { DaggerOnBoardingComponent.builder().appComponent(injector).build() }

    override val viewModel by lazyViewModel { featureInjector.onBoardingViewModel }

    override fun getLayoutRes(): Int {
        return R.layout.frg_onboarding
    }

    override fun setViewModelInBinding(binding: FrgOnboardingBinding, viewModel: OnBoardingViewModel) {
        binding.viewModel = viewModel
    }
}