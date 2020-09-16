package com.core.wumfapp2020.fragment

import android.os.Bundle
import android.view.View
import com.core.wumfapp2020.R
import com.core.wumfapp2020.VisibleBottomTabsState
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.base.showLogOutDialog
import com.core.wumfapp2020.databinding.FrgProfileBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.ProfileViewModel
import com.library.core.lazySavedStateViewModel
import kotlin.system.exitProcess

class ProfileFragment : AppBaseFragment<FrgProfileBinding, ProfileViewModel>(R.layout.frg_profile) {

    override val viewModel by lazySavedStateViewModel {
        injector.profileViewModel
    }

    override val bottomTabs = VisibleBottomTabsState(3)

    override fun setViewModelInBinding(binding: FrgProfileBinding, viewModel: ProfileViewModel) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEvent(viewModel.logOut) {
            showLogOutDialog(requireContext()) {
                if (it) {
                    viewModel.logOut {
                        requireActivity().finishAffinity()
                        exitProcess(0)
                    }
                }
            }
        }
    }

}