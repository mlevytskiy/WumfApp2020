package com.core.wumfapp2020.base

import androidx.databinding.ViewDataBinding
import com.core.wumfapp2020.MainActivity
import com.library.core.BaseFragment
import com.library.core.BaseViewModel
import com.core.wumfapp2020.BottomTabsState
import com.core.wumfapp2020.GoneBottomTabsState

abstract class AppBaseFragment<B : ViewDataBinding, VM : BaseViewModel>(uiRes: Int): BaseFragment<B, VM>(uiRes) {

    protected open val bottomTabs: BottomTabsState =
        GoneBottomTabsState

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as MainActivity
        mainActivity.setBottomTabsState(bottomTabs)
    }
}