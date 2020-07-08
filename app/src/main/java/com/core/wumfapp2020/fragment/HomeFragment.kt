package com.core.wumfapp2020.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.core.wumfapp2020.R
import com.core.wumfapp2020.VisibleBottomTabsState
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.base.countriesdialog.CountriesHolder
import com.core.wumfapp2020.base.showAppDialog
import com.core.wumfapp2020.base.showCountriesDialog
import com.core.wumfapp2020.base.showSimpleDialog
import com.core.wumfapp2020.databinding.FrgHomeBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.*
import com.core.wumfapp2020.viewmodel.home.HomeTitle
import com.library.core.lazySavedStateViewModel
import wumf.com.detectphone.AppCountryDetector
import wumf.com.detectphone.Country

class HomeFragment : AppBaseFragment<FrgHomeBinding, HomeViewModel>(R.layout.frg_home) {

    override val viewModel by lazySavedStateViewModel {
        injector.homeViewModelFactory.create(it)
    }

    override val bottomTabs = VisibleBottomTabsState(0)

    override fun setViewModelInBinding(binding: FrgHomeBinding, viewModel: HomeViewModel) {
        binding.viewModel = viewModel
        viewModel.loadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appsRecycleView.showLoadedState()
        observeEvent(viewModel.showPickAppCategoryDialog) {
            val selected = detectChekedItemInPickAppCategoryDialog(it)
            showPickAppCategoryDialog(selected)
        }
        observeEvent(viewModel.showCountriesDialog) {
            showCountryDialog(it)
        }
        observeEvent(viewModel.showPickedApps) {
            binding.appsRecycleView.setPackages(it.appPackages, it.likes)
        }
        var dialog : DialogInterface? = null
        val context = view.context
        binding.appsRecycleView.setItemClick { app, likes ->
            dialog = showAppDialog(app, context,
                {
                    dialog?.dismiss()
                    showInGooglePlay(app.packageName, context)
                }, {
                    dialog?.dismiss()
                    viewModel.navigateToPeopleWhoLikes()
                }, likes
            )
        }
    }

    fun showInGooglePlay(pkg: String, context: Context) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$pkg")))
        } catch (anfe: android.content.ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$pkg")
                )
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
        dialog = showCountriesDialog(requireContext(), countriesHolder.countries, -1, { country ->
            dialog?.dismiss()
            viewModel.pickedTypeOfApps(IN_ANOTHER_COUNTRY, country)
        }, {})
    }

}