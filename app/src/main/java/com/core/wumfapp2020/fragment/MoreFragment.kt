package com.core.wumfapp2020.fragment

import com.core.wumfapp2020.R
import com.core.wumfapp2020.VisibleBottomTabsState
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.databinding.FrgMoreBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.MoreViewModel
import com.library.core.lazySavedStateViewModel

class MoreFragment : AppBaseFragment<FrgMoreBinding, MoreViewModel>(R.layout.frg_more) {

    override val viewModel by lazySavedStateViewModel {
        injector.moreViewModel
    }

    override val bottomTabs = VisibleBottomTabsState(4)

    override fun setViewModelInBinding(binding: FrgMoreBinding, viewModel: MoreViewModel) {
        binding.viewModel = viewModel
    }

}