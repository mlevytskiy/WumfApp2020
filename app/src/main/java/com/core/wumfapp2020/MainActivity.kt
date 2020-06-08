package com.core.wumfapp2020

import com.core.wumfapp2020.databinding.ActivityMainBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.MainActivityViewModel
import com.library.core.BaseActivity
import com.library.core.di.lazyViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>(R.layout.activity_main, R.id.main_nav_host) {

    override val viewModel by lazyViewModel { injector.mainActivityViewModel }

    fun setBottomTabsState(bottomTabs: BottomTabsState) {
        viewModel.showBottomTabs.set(bottomTabs != GoneBottomTabsState)
    }

    override fun setViewModelInBinding(binding: ActivityMainBinding, viewModel: MainActivityViewModel) {
        binding.viewModel = viewModel
    }

}
