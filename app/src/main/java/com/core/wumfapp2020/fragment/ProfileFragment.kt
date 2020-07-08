package com.core.wumfapp2020.fragment

import com.core.wumfapp2020.R
import com.core.wumfapp2020.VisibleBottomTabsState
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.databinding.FrgProfileBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.ProfileViewModel
import com.library.core.lazySavedStateViewModel

class ProfileFragment : AppBaseFragment<FrgProfileBinding, ProfileViewModel>(R.layout.frg_profile) {

    override val viewModel by lazySavedStateViewModel {
        injector.profileViewModel
    }

    override val bottomTabs = VisibleBottomTabsState(3)

    override fun setViewModelInBinding(binding: FrgProfileBinding, viewModel: ProfileViewModel) {
        binding.viewModel = viewModel
    }

}