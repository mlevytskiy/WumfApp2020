package com.core.dynamicfeature.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.core.dynamicfeature.R
import com.core.dynamicfeature.databinding.FrgEnterPhoneNumberBinding
import com.core.dynamicfeature.di.featureInjector
import com.core.dynamicfeature.viewmodel.EnterPhoneNumberViewModel
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.viewmodel.ResultStatus
import com.library.core.lazyViewModel

class EnterPhoneNumberFragment: AppBaseFragment<FrgEnterPhoneNumberBinding, EnterPhoneNumberViewModel>(
    R.layout.frg_enter_phone_number) {

    override val viewModel by lazyViewModel {
        featureInjector.enterPhoneNumberViewModelFactory.create()
    }

    override fun setViewModelInBinding(binding: FrgEnterPhoneNumberBinding, viewModel: EnterPhoneNumberViewModel) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.moveToDetectPhoneNumberIfNeed()
        observeEvent(viewModel.showNextButtonInProgressState) {
            binding.nextButton.isIndeterminateProgressMode = true
            binding.nextButton.progress = 50
        }
        observeEvent(viewModel.showEnterCodeState) {
            binding.phoneNumberFlipWrapper.showBackCard()
            binding.nextButton.progress = 0
        }
        observeEvent(viewModel.showSuccess) {
            val mcc = binding.phoneNumberFlipWrapper.getCountryMCC()
            viewModel.saveCountryMCC(mcc)
            binding.nextButton.progress = 100
        }
    }

    override fun onVisible() {
        viewModel.setPhoneNumberFromMemory()
    }

}