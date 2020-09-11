package com.core.wumfapp2020

import android.os.Bundle
import com.core.wumfapp2020.base.showErrorDialog
import com.core.wumfapp2020.databinding.ActivityMainBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.util.setupWithNavControllerWithoutAnimation
import com.core.wumfapp2020.viewmodel.HomeViewModel
import com.core.wumfapp2020.viewmodel.MainActivityViewModel
import com.library.core.BaseActivity
import com.library.core.BaseViewModel
import com.library.core.lazyViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>(R.layout.activity_main, R.id.main_nav_host) {

    override val viewModel by lazyViewModel { injector.mainActivityViewModel }

    var home: HomeViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.isSkipOnBoarding()) {
            viewModel.updateToken()
            makeHomeStart()
        }
        observeEvent(BaseViewModel.syncMyApps) {
            viewModel.syncMyCollection()
        }
        observeEvent(viewModel.showErrorMessage) {
            showErrorDialog(this, it)
        }
    }

    fun setBottomTabsState(bottomTabs: BottomTabsState) {
        viewModel.showBottomTabs.set(bottomTabs != GoneBottomTabsState)
    }

    override fun setViewModelInBinding(binding: ActivityMainBinding, viewModel: MainActivityViewModel) {
        binding.viewModel = viewModel
        binding.homeBottomNav.setupWithNavControllerWithoutAnimation(navController)
    }

    fun setInternetStaus(enable: Boolean) {
        viewModel.hasInternetConnection.set(enable)
    }

    fun makeHomeStart() {
        navController.setGraph(R.navigation.main_graph)
    }

}
