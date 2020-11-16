package com.core.wumfapp2020.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.core.wumfapp2020.R
import com.core.wumfapp2020.VisibleBottomTabsState
import com.core.wumfapp2020.base.TabFragment
import com.core.wumfapp2020.base.showAppDialogFromMyCollection
import com.core.wumfapp2020.databinding.FrgAppsBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.util.showInGooglePlay
import com.core.wumfapp2020.viewmodel.AppsViewModel
import com.library.core.lazySavedStateViewModel
import com.library.core.log

class AppsFragment : TabFragment<FrgAppsBinding, AppsViewModel>(R.layout.frg_apps) {

    override val viewModel by lazySavedStateViewModel {
        injector.appsViewModel
    }

    override val bottomTabs = VisibleBottomTabsState(1)

    override fun setViewModelInBinding(binding: FrgAppsBinding, viewModel: AppsViewModel) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState(viewModel.showPickedApps) {
            "it.appPackages=${it.appPackages}".log()
            binding.appsRecycleView.setPackages(it.appPackages, it.likes)
        }
        var dialog : DialogInterface? = null
        val context = view.context
        binding.appsRecycleView.setItemClick { app, likes ->
            dialog = showAppDialogFromMyCollection(app, context,
                {
                    dialog?.dismiss()
                    context.showInGooglePlay(app.packageName)
                }, {
                    viewModel.removeAppFromMemory(app.packageName)
                    dialog?.dismiss()
                }
            )
        }
        viewModel.updateData()
    }
}