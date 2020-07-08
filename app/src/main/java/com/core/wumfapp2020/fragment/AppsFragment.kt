package com.core.wumfapp2020.fragment

import com.core.wumfapp2020.R
import com.core.wumfapp2020.VisibleBottomTabsState
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.databinding.FrgAppsBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.AppsViewModel
import com.library.core.lazySavedStateViewModel

class AppsFragment : AppBaseFragment<FrgAppsBinding, AppsViewModel>(R.layout.frg_apps) {

    override val viewModel by lazySavedStateViewModel {
        injector.appsViewModel
    }

    override val bottomTabs = VisibleBottomTabsState(1)

    override fun setViewModelInBinding(binding: FrgAppsBinding, viewModel: AppsViewModel) {
        binding.viewModel = viewModel
    }

}