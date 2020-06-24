package com.core.wumfapp2020.fragment

import com.core.wumfapp2020.R
import com.core.wumfapp2020.VisibleBottomTabsState
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.databinding.FrgHomeBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.HomeViewModel
import com.library.core.di.lazyViewModel

class HomeFragment : AppBaseFragment<FrgHomeBinding, HomeViewModel>(R.layout.frg_home) {

    override val viewModel by lazyViewModel { injector.homeViewModel }

    override val bottomTabs = VisibleBottomTabsState(0)

    override fun setViewModelInBinding(binding: FrgHomeBinding, viewModel: HomeViewModel) {
        binding.viewModel = viewModel
    }

}