package com.core.wumfapp2020.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavOptions
import com.core.wumfapp2020.BottomTabsState
import com.core.wumfapp2020.GoneBottomTabsState
import com.core.wumfapp2020.MainActivity
import com.core.wumfapp2020.R
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.AnyFragmentBaseViewModel
import com.library.core.BaseFragment


abstract class AppBaseFragment<B : ViewDataBinding, VM : AnyFragmentBaseViewModel>(uiRes: Int): BaseFragment<B, VM>(uiRes) {

    protected open val bottomTabs: BottomTabsState =
        GoneBottomTabsState

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as MainActivity
        mainActivity.setBottomTabsState(bottomTabs)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        injector.inject(viewModel)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEvent(viewModel.sendInternetStatus) {
            val mainActivity = requireActivity() as MainActivity
            mainActivity.setInternetStaus(it)
        }
        observeEvent(viewModel.showErrorMessage) {
            showErrorDialog(requireContext(), it)
        }
    }

    override fun getNavOptions(navOptionsByDirection: NavOptions?): NavOptions? {
        val builder = NavOptions.Builder()
            .setEnterAnim(R.anim.push_left_in)
            .setExitAnim(R.anim.push_left_out)
            .setPopEnterAnim(R.anim.push_right_in)
            .setPopExitAnim(R.anim.push_right_out)
        navOptionsByDirection?.let {
            builder.setPopUpTo(it.popUpTo, it.isPopUpToInclusive)
            builder.setLaunchSingleTop(it.shouldLaunchSingleTop())
        }
        return builder.build()
    }
}