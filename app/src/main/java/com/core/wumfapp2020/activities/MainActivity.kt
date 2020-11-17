package com.core.wumfapp2020.activities

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.SparseArray
import android.view.View
import androidx.core.view.ViewCompat
import com.core.wumfapp2020.BottomTabsState
import com.core.wumfapp2020.DataBindingAdapters
import com.core.wumfapp2020.GoneBottomTabsState
import com.core.wumfapp2020.R
import com.core.wumfapp2020.base.showErrorDialog
import com.core.wumfapp2020.databinding.ActivityMainBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.util.setupWithNavControllerWithoutAnimation
import com.core.wumfapp2020.viewmodel.HomeViewModel
import com.core.wumfapp2020.viewmodel.act.MainViewModel
import com.library.core.BaseActivity
import com.library.core.BaseViewModel
import com.library.core.lazyViewModel
import java.lang.ref.WeakReference

class MainActivity : ChangeLanguageActivity2<ActivityMainBinding, MainViewModel>(R.layout.activity_main, R.id.main_nav_host) {

    override val viewModel by lazyViewModel { injector.mainViewModel }

    var home: HomeViewModel? = null

    val tabViews = SparseArray<WeakReference<View?>?>()

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
        val root = findViewById<View>(R.id.root)

        DataBindingAdapters.statusBarHeight = toPixels(resources.getDimension(R.dimen.spacing_24))
        ViewCompat.requestApplyInsets(root)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            root.setOnApplyWindowInsetsListener(null)
            DataBindingAdapters.statusBarHeight = insets.systemWindowInsetTop
            insets}

        val bottomTabsView = findViewById<View>(R.id.home_bottom_nav)
        bottomTabsView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        DataBindingAdapters.bottomTabsHeight = bottomTabsView.measuredHeight
    }

    fun setBottomTabsState(bottomTabs: BottomTabsState) {
        viewModel.showBottomTabs.set(bottomTabs != GoneBottomTabsState)
    }

    override fun setViewModelInBinding(binding: ActivityMainBinding, viewModel: MainViewModel) {
        binding.viewModel = viewModel
        binding.homeBottomNav.setupWithNavControllerWithoutAnimation(navController)
    }

    fun setInternetStaus(enable: Boolean) {
        viewModel.hasInternetConnection.set(enable)
    }

    fun makeHomeStart() {
        navController.setGraph(R.navigation.main_graph)
    }

    fun Context.toPixels(dp: Float): Int {
        return (dp * (this.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

}
