package com.core.wumfapp2020.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.core.wumfapp2020.MainActivity
import com.core.wumfapp2020.R
import com.core.wumfapp2020.VisibleBottomTabsState
import com.core.wumfapp2020.base.*
import com.core.wumfapp2020.base.countriesdialog.CountriesHolder
import com.core.wumfapp2020.databinding.FrgHomeBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.util.showInGooglePlay
import com.core.wumfapp2020.viewmodel.*
import com.core.wumfapp2020.viewmodel.home.HomeTitle
import com.library.core.lazySavedStateViewModel
import wumf.com.detectphone.AppCountryDetector
import wumf.com.detectphone.Country
import java.lang.ref.WeakReference

class HomeFragment : TabFragment<FrgHomeBinding, HomeViewModel>(R.layout.frg_home) {

    override val viewModel by lazySavedStateViewModel { state->
        injector.homeViewModelFactory.create()
    }

    override val bottomTabs = VisibleBottomTabsState(0)

    override fun setViewModelInBinding(binding: FrgHomeBinding, viewModel: HomeViewModel) {
        binding.viewModel = viewModel
        viewModel.loadDataIfMemoryEmpty()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState(viewModel.showPickAppCategoryDialog) {
            val selected = detectChekedItemInPickAppCategoryDialog(it)
            showPickAppCategoryDialog(selected)
        }
        observeState(viewModel.showCountriesDialog) {
            showCountryDialog(it)
        }
        observeState(viewModel.showPickedApps) {
            binding.appsRecycleView.setPackages(it.appPackages, it.likes)
        }
        var dialog : DialogInterface? = null
        val context = view.context
        binding.appsRecycleView.setItemClick { app, likes ->
            dialog = showAppDialog(app, context,
                {
                    dialog?.dismiss()
                    context.showInGooglePlay(app.packageName)
                }, {
                    dialog?.dismiss()
                    viewModel.navigateToPeopleWhoLikes(app = app, likes = likes.toIntArray())
                }, likes
            )
        }
    }

    private fun detectChekedItemInPickAppCategoryDialog(type: HomeTitle.Type): Int {
        when(type) {
            HomeTitle.Type.IN_THE_WORLD -> {
                return IN_THE_WORLD
            }
            HomeTitle.Type.IN_MY_COUNTRY -> {
                return IN_MY_COUNTRY
            }
            HomeTitle.Type.IN_ANOTHER_COUNTRY -> {
                return IN_ANOTHER_COUNTRY
            }
            HomeTitle.Type.AMONG_FRIENDS -> {
                return AMONG_FRIENDS
            }
        }
    }

    private fun showPickAppCategoryDialog(checkedItem: Int) { //in the world; in country; among friends
        if (AppCountryDetector.isMapsEmpty()) {
            AppCountryDetector.fillMap(requireContext())
        }
        showSimpleDialog(requireContext(), R.array.type_of_apps, checkedItem,
            { _, pos ->
                val country: Country? = if (pos == IN_MY_COUNTRY) viewModel.getDefaultCountry()
                else null
                viewModel.pickedTypeOfApps(pos, country)
            }, {
                it.dismiss()
            }, viewModel.getDefaultCountry()?.name?:"")
    }

    private fun showCountryDialog(countriesHolder: CountriesHolder) {
        var dialog: DialogInterface? = null
        if (countriesHolder.countries.isEmpty()) {
            countriesHolder.syncLoad()
        }
        dialog = showCountriesDialog(context = requireContext(), countries = countriesHolder.countries, checkedItem = -1, select = { country ->
            dialog?.dismiss()
            viewModel.pickedTypeOfApps(IN_ANOTHER_COUNTRY, country)
        })
    }

}