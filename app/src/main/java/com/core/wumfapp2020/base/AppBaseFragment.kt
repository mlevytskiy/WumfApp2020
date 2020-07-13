package com.core.wumfapp2020.base

import androidx.databinding.ViewDataBinding
import androidx.navigation.NavOptions
import com.core.wumfapp2020.BottomTabsState
import com.core.wumfapp2020.GoneBottomTabsState
import com.core.wumfapp2020.MainActivity
import com.core.wumfapp2020.R
import com.library.core.BaseFragment
import com.library.core.BaseViewModel


abstract class AppBaseFragment<B : ViewDataBinding, VM : BaseViewModel>(uiRes: Int): BaseFragment<B, VM>(uiRes) {

    protected open val bottomTabs: BottomTabsState =
        GoneBottomTabsState

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as MainActivity
        mainActivity.setBottomTabsState(bottomTabs)
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