package com.core.wumfapp2020.fragment

import com.core.wumfapp2020.R
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.databinding.FrgPeopleWhoLikesBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.PeopleWhoLikesViewModel
import com.library.core.lazySavedStateViewModel

class PeopleWhoLikesFragment : AppBaseFragment<FrgPeopleWhoLikesBinding, PeopleWhoLikesViewModel>(R.layout.frg_people_who_likes) {

    override val viewModel by lazySavedStateViewModel {
        injector.peopleWhoLikesViewModel
    }

    override fun setViewModelInBinding(binding: FrgPeopleWhoLikesBinding, viewModel: PeopleWhoLikesViewModel) {
        binding.viewModel = viewModel
    }

}