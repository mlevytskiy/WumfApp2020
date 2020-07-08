package com.core.wumfapp2020.fragment

import android.os.Bundle
import androidx.navigation.dynamicfeatures.DynamicInstallMonitor
import com.core.wumfapp2020.R
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.databinding.FrgPreOnBoardingBinding
import com.core.wumfapp2020.viewmodel.PreOnBoardingViewModel
import com.library.core.lazyViewModel
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.ResultStatus

private const val REQUEST_CODE = 12341

class PreOnBoardingFragment : AppBaseFragment<FrgPreOnBoardingBinding, PreOnBoardingViewModel>(R.layout.frg_pre_on_boarding) {

    private val installMonitor = DynamicInstallMonitor()

    override val viewModel by lazyViewModel { injector.preOnBoardingViewModel }

    override fun setViewModelInBinding(binding: FrgPreOnBoardingBinding, viewModel: PreOnBoardingViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.sharedViewModel.status?.let {
            viewModel.handleOnBoardingResult(it == ResultStatus.SUCCESS)
            viewModel.sharedViewModel.status = null
        }
    }

}