package com.core.dynamicfeature.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.core.dynamicfeature.R
import com.core.dynamicfeature.databinding.FrgEnterPhoneNumberBinding
import com.core.dynamicfeature.databinding.FrgEnterTelegramPasswordBinding
import com.core.dynamicfeature.di.featureInjector
import com.core.dynamicfeature.viewmodel.EnterPhoneNumberViewModel2
import com.core.dynamicfeature.viewmodel.EnterTelegramPasswordViewModel
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.viewmodel.ResultStatus
import com.google.android.play.core.internal.by
import com.library.core.lazyViewModel
import com.library.core.showKeyboard
import kotlinx.android.synthetic.main.frg_enter_phone_number.*
import krafts.alex.tg.TgClient

class EnterTelegramPasswordFragment : AppBaseFragment<FrgEnterTelegramPasswordBinding, EnterTelegramPasswordViewModel>(R.layout.frg_enter_telegram_password) {

    override val viewModel by lazyViewModel {
        val args: EnterTelegramPasswordFragmentArgs by navArgs()
        featureInjector.enterTelegramPasswordViewModelFactory.create(args.passwordHint)
    }

    override fun setViewModelInBinding(binding: FrgEnterTelegramPasswordBinding, viewModel: EnterTelegramPasswordViewModel) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.title = "Two-Step Verification"
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setupWithNavController(navController, null)
        binding.passwordEditText.showKeyboard()
    }

}