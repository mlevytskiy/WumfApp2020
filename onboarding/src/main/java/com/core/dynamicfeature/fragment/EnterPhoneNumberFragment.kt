package com.core.dynamicfeature.fragment

import com.core.dynamicfeature.R
import com.core.dynamicfeature.databinding.FrgDetectingPhoneNumberBinding
import com.core.dynamicfeature.databinding.FrgEnterPhoneNumberBinding
import com.core.dynamicfeature.di.featureInjector
import com.core.dynamicfeature.viewmodel.DetectingYourPhoneNumberViewModel
import com.core.dynamicfeature.viewmodel.EnterPhoneNumberViewModel
import com.core.wumfapp2020.base.AppBaseFragment
import com.library.core.di.lazyViewModel

class EnterPhoneNumberFragment: AppBaseFragment<FrgEnterPhoneNumberBinding, EnterPhoneNumberViewModel>(
    R.layout.frg_enter_phone_number) {

    override val viewModel by lazyViewModel { featureInjector.enterPhoneNumberViewModelFactory.create() }

    override fun setViewModelInBinding(binding: FrgEnterPhoneNumberBinding, viewModel: EnterPhoneNumberViewModel) {
        binding.viewModel = viewModel
    }

}