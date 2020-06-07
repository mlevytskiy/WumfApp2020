package com.core.dynamicfeature.fragment

import androidx.navigation.fragment.navArgs
import com.core.dynamicfeature.R
import com.core.dynamicfeature.databinding.FrgOnboardingBinding
import com.core.dynamicfeature.viewmodel.OnBoardingViewModel
import com.library.core.di.lazyViewModel


class OnBoardingFragment: AnyOnBoardingFragment<FrgOnboardingBinding, OnBoardingViewModel>() {

    override val uiRes = R.layout.frg_onboarding
    override val viewModel by lazyViewModel {
        val args: OnBoardingFragmentArgs by navArgs()
        featureInjector.onBoardingViewModelFactory.create(args.someArg)
    }

    override fun setViewModelInBinding(binding: FrgOnboardingBinding, viewModel: OnBoardingViewModel) {
        binding.viewModel = viewModel
    }

}