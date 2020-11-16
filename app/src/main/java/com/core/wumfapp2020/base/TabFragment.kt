package com.core.wumfapp2020.base

import android.view.View
import androidx.databinding.ViewDataBinding
import com.core.wumfapp2020.activities.MainActivity
import com.core.wumfapp2020.VisibleBottomTabsState
import com.core.wumfapp2020.viewmodel.AnyFragmentBaseViewModel
import java.lang.ref.WeakReference

abstract class TabFragment<B : ViewDataBinding, VM : AnyFragmentBaseViewModel>(uiRes: Int): AppBaseFragment<B, VM>(uiRes)  {

    override fun getReusedView(): View? {
        return (activity as MainActivity).tabViews[tabIndex()]?.get()
    }

    override fun setReusedView(view: View) {
        (activity as MainActivity).tabViews.put(tabIndex(), WeakReference(view))
    }

    private fun tabIndex(): Int {
        return (bottomTabs as VisibleBottomTabsState).selectedTab
    }

}