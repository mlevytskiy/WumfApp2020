package com.core.wumfapp2020.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.ui.setupWithNavController
import com.core.wumfapp2020.R
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.base.showCheckAppIfExistOnGooglePlayDialog
import com.core.wumfapp2020.databinding.FrgAddAppInMyCollectionBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.AddAppInMyCollectionViewModel
import com.library.core.lazySavedStateViewModel

class AddAppInMyCollectionFragment : AppBaseFragment<FrgAddAppInMyCollectionBinding, AddAppInMyCollectionViewModel>(R.layout.frg_add_app_in_my_collection) {

    override val viewModel by lazySavedStateViewModel {
        injector.addAppInMyCollectionViewModel
    }

    override fun setViewModelInBinding(binding: FrgAddAppInMyCollectionBinding, viewModel: AddAppInMyCollectionViewModel) {
        binding.viewModel = viewModel
        binding.toolbar.setupWithNavController(navController, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appsRecycleView.setItemClick { appContainer, _ ->
            showCheckAppIfExistOnGooglePlayDialog(requireContext(), appContainer, { viewModel.addAppToMyCollection(appContainer.packageName) },
                { viewModel.moveToMyCollectionScreen() } )
        }
    }

}