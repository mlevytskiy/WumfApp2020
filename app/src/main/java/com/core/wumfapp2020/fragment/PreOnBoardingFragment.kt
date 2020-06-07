package com.core.wumfapp2020.fragment

import android.os.Bundle
import androidx.navigation.dynamicfeatures.DynamicExtras
import androidx.navigation.dynamicfeatures.DynamicInstallMonitor
import androidx.navigation.fragment.findNavController
import com.core.wumfapp2020.R
import com.core.wumfapp2020.databinding.FrgPreOnBoardingBinding
import com.core.wumfapp2020.viewmodel.PreOnBoardingViewModel
import com.library.core.BaseFragment
import com.library.core.di.lazyViewModel
import com.core.wumfapp2020.di.injector
import com.library.EventObserver


private const val REQUEST_CODE = 12341

class PreOnBoardingFragment : BaseFragment<FrgPreOnBoardingBinding, PreOnBoardingViewModel>() {

    private val installMonitor = DynamicInstallMonitor()

    override val uiRes = R.layout.frg_pre_on_boarding
    override val viewModel by lazyViewModel { injector.preOnBoardingViewModel }

    override fun setViewModelInBinding(binding: FrgPreOnBoardingBinding, viewModel: PreOnBoardingViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.moveToOnBoarding.observe(this, EventObserver {
            navigateToOnBoarding()
        })
    }

    fun navigateToOnBoarding() {
        findNavController().navigate(
            PreOnBoardingFragmentDirections.actionPreOnBoardingToOnBoarding("testArg"),
            DynamicExtras(installMonitor))
//        inline fun NavController?.safeNavigate(context: Context?, navDirections: NavDirections?) {
//            navDirections?.let { direction ->
//                this?.currentDestination?.getAction(direction.actionId)?.let {
//                    this.navigate(direction)
//                } ?: log("Skip nav to: $direction\n" +
//                        "  [ Can't find ${context.getResName(direction.actionId)} in ${context.getResName(this?.currentDestination?.id)} ]")
//            }
//        }
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