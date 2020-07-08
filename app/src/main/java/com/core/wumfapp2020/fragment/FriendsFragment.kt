package com.core.wumfapp2020.fragment

import com.core.wumfapp2020.R
import com.core.wumfapp2020.VisibleBottomTabsState
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.databinding.FrgFriendsBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.FriendsViewModel
import com.library.core.lazySavedStateViewModel

class FriendsFragment : AppBaseFragment<FrgFriendsBinding, FriendsViewModel>(R.layout.frg_friends) {

    override val viewModel by lazySavedStateViewModel {
        injector.friendsViewModel
    }

    override val bottomTabs = VisibleBottomTabsState(2)

    override fun setViewModelInBinding(binding: FrgFriendsBinding, viewModel: FriendsViewModel) {
        binding.viewModel = viewModel
    }

}