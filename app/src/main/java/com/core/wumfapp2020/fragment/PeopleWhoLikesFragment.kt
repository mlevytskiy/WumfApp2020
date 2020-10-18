package com.core.wumfapp2020.fragment

import androidx.databinding.ObservableArrayList
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.core.wumfapp2020.R
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.databinding.FrgPeopleWhoLikesBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.FriendsViewModel
import com.core.wumfapp2020.viewmodel.PeopleWhoLikesViewModel
import com.library.core.lazySavedStateViewModel
import wumf.com.appsprovider2.AppContainer

class PeopleWhoLikesFragment : AppBaseFragment<FrgPeopleWhoLikesBinding, PeopleWhoLikesViewModel>(R.layout.frg_people_who_likes) {

    override val viewModel by lazySavedStateViewModel {
        val args: PeopleWhoLikesFragmentArgs by navArgs()
        injector.peopleWhoLikesViewModelFactory.create(args.app, args.likes)
    }

    override fun setViewModelInBinding(binding: FrgPeopleWhoLikesBinding, viewModel: PeopleWhoLikesViewModel) {
        binding.viewModel = viewModel
        binding.toolbar.setupWithNavController(navController, null)
        binding.toolbar.navigationIcon = requireContext().getDrawable(R.drawable.ic_arrow_white)
    }

}