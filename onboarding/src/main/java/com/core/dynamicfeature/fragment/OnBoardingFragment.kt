package com.core.dynamicfeature.fragment

import com.core.dynamicfeature.R
import com.core.dynamicfeature.databinding.FrgOnboardingBinding
import com.core.dynamicfeature.di.featureInjector
import com.core.dynamicfeature.viewmodel.OnBoardingViewModel
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.VisibleBottomTabsState
import com.library.core.lazyViewModel

class OnBoardingFragment: AppBaseFragment<FrgOnboardingBinding, OnBoardingViewModel>(R.layout.frg_onboarding) {

//    val args: OnBoardingFragmentArgs by navArgs()

    override val bottomTabs = VisibleBottomTabsState(0)

    override val viewModel by lazyViewModel { featureInjector.onBoardingViewModelFactory.create() }

    override fun setViewModelInBinding(binding: FrgOnboardingBinding, viewModel: OnBoardingViewModel) {
        binding.viewModel = viewModel
    }

}