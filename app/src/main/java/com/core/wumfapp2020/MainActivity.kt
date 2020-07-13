package com.core.wumfapp2020

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.core.wumfapp2020.databinding.ActivityMainBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.MainActivityViewModel
import com.library.core.BaseActivity
import com.library.core.lazyViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>(R.layout.activity_main, R.id.main_nav_host) {

    override val viewModel by lazyViewModel { injector.mainActivityViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.isSkipOnBoarding()) {
            makeHomeStart()
        }
    }

    fun setBottomTabsState(bottomTabs: BottomTabsState) {
        viewModel.showBottomTabs.set(bottomTabs != GoneBottomTabsState)
    }

    override fun setViewModelInBinding(binding: ActivityMainBinding, viewModel: MainActivityViewModel) {
        binding.viewModel = viewModel
        binding.homeBottomNav.setupWithNavController(Navigation.findNavController(this, R.id.main_nav_host))
    }

    fun makeHomeStart() {
        navController.setGraph(R.navigation.main_graph)
    }
}
