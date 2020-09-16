package com.core.dynamicfeature.fragment

import android.os.Bundle
import android.view.View
import com.core.dynamicfeature.R
import com.core.dynamicfeature.databinding.FrgEnterPhoneNumberBinding
import com.core.dynamicfeature.di.featureInjector
import com.core.dynamicfeature.viewmodel.EnterPhoneNumberViewModel2
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.base.showSuccessLoginDialog
import com.dd.State
import com.library.core.lazyViewModel
import com.library.core.log

class EnterPhoneNumberFragment: AppBaseFragment<FrgEnterPhoneNumberBinding, EnterPhoneNumberViewModel2>(
    R.layout.frg_enter_phone_number) {

    override val viewModel by lazyViewModel {
        featureInjector.enterPhoneNumberViewModelFactory.create()
    }

    override fun setViewModelInBinding(binding: FrgEnterPhoneNumberBinding, viewModel: EnterPhoneNumberViewModel2) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.moveToDetectPhoneNumberIfNeed()) {
            return
        }
        viewModel.testNav()
        observeEvent(viewModel.showNextButtonInNormalState) {
            binding.nextButton.progress = 0
        }
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
            showSuccessLoginDialog(requireContext(), it.image, it.name, it.contactsAmount, it.telegramId, it.phoneNumber)
        }
        observeState(viewModel.loginToTelegram) {
            when(it) {
                EnterPhoneNumberViewModel2.LoginToTelegram.ENTER_CODE -> {
                    viewModel.showEnterCodeUI()
                }
            }
        }
    }

    override fun onVisible() {
        viewModel.setPhoneNumberFromMemory()
        viewModel.handleOnBoardingResult()
    }

}