package com.core.wumfapp2020.fragment

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.widget.Toast
import com.core.wumfapp2020.R
import com.core.wumfapp2020.databinding.FrgPreOnBoardingBinding
import com.core.wumfapp2020.event.OpenOnBoarding
import com.core.wumfapp2020.viewmodel.PreOnBoardingViewModel
import com.library.core.BaseFragment
import org.greenrobot.eventbus.Subscribe
import kotlin.reflect.KClass


private const val REQUEST_CODE = 12341

class PreOnBoardingFragment : BaseFragment<FrgPreOnBoardingBinding, PreOnBoardingViewModel>() {

    override fun getViewModelClass(): KClass<PreOnBoardingViewModel> {
        return PreOnBoardingViewModel::class
    }

    override fun setViewModelInBinding(binding: FrgPreOnBoardingBinding, viewModel: PreOnBoardingViewModel) {
        binding.viewModel = viewModel
    }

    override fun onInitVM() { }

    override fun getLayoutRes(): Int {
        return R.layout.frg_pre_on_boarding
    }

    override fun onResume() {
        super.onResume()
        viewModel?.checkInternetConnection()
    }

    @Subscribe
    fun onEvent(event: OpenOnBoarding) {
        val intent = Intent()
        intent.setClassName("com.core.wumfapp2020", "com.core.dynamicfeature.OnBoardingActivity2")
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(requireContext(), "we get result ok", Toast.LENGTH_LONG).show()
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(requireContext(), "canceled", Toast.LENGTH_LONG).show()
            }
        }
    }

}