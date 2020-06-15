package com.core.wumfapp2020.fragment

import android.os.Bundle
import androidx.navigation.dynamicfeatures.DynamicExtras
import androidx.navigation.dynamicfeatures.DynamicInstallMonitor
import androidx.navigation.fragment.findNavController
import com.core.wumfapp2020.R
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.databinding.FrgPreOnBoardingBinding
import com.core.wumfapp2020.viewmodel.PreOnBoardingViewModel
import com.library.core.di.lazyViewModel
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

//    override fun getViewModelClass(): KClass<PreOnBoardingViewModel> {
//        return PreOnBoardingViewModel::class
//    }
//
//    override fun setViewModelInBinding(binding: FrgPreOnBoardingBinding, viewModel: PreOnBoardingViewModel) {
//        binding.viewModel = viewModel
//    }
//
//    override fun onInitVM() { }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.frg_pre_on_boarding
//    }
//
//    override fun onResume() {
//        super.onResume()
//        viewModel?.checkInternetConnection()
//    }
//
//    @Subscribe
//    fun onEvent(event: OpenOnBoarding) {
//        val intent = Intent()
//        intent.setClassName("com.core.wumfapp2020", "com.core.dynamicfeature.OnBoardingActivity2")
//        startActivity(intent)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Toast.makeText(requireContext(), "we get result ok", Toast.LENGTH_LONG).show()
//            } else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(requireContext(), "canceled", Toast.LENGTH_LONG).show()
//            }
//        }
//    }

}