package com.core.wumfapp2020.fragment

import android.graphics.Color
import android.os.Handler
import android.view.WindowManager
import androidx.navigation.dynamicfeatures.DynamicInstallMonitor
import com.core.wumfapp2020.R
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.databinding.FrgPreOnBoardingBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.PreOnBoardingViewModel
import com.core.wumfapp2020.viewmodel.ResultStatus
import com.library.core.lazyViewModel

private const val REQUEST_CODE = 12341

class PreOnBoardingFragment : AppBaseFragment<FrgPreOnBoardingBinding, PreOnBoardingViewModel>(R.layout.frg_pre_on_boarding) {

    private val installMonitor = DynamicInstallMonitor()
    private val handler = Handler()

    override val viewModel by lazyViewModel { injector.preOnBoardingViewModel }

    override fun setViewModelInBinding(binding: FrgPreOnBoardingBinding, viewModel: PreOnBoardingViewModel) {
        binding.viewModel = viewModel
    }

    override fun onVisible() {
        viewModel.handleOnBoardingResult(viewModel.sharedViewModel.status == ResultStatus.SUCCESS)
        viewModel.sharedViewModel.status = null
        requireActivity().window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.BLACK
        }
//        viewModel.sharedViewModel.status?.let {
//            viewModel.handleOnBoardingResult(it == ResultStatus.SUCCESS)
//            viewModel.sharedViewModel.status = null
//        }
//        handler.postDelayed( {
//            viewModel.sharedViewModel.status?.let {
//                viewModel.handleOnBoardingResult(it == ResultStatus.SUCCESS)
//                viewModel.sharedViewModel.status = null
//            }
//        }, 500) //onVisible called before PreOnBoarding stayed visible in case when we return from on_boarding_graph
    }

}