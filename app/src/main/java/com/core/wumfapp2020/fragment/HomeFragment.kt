package com.core.wumfapp2020.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.core.wumfapp2020.R
import com.core.wumfapp2020.VisibleBottomTabsState
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.base.countriesdialog.CountriesHolder
import com.core.wumfapp2020.base.countriesdialog.Country
import com.core.wumfapp2020.base.showCountriesDialog
import com.core.wumfapp2020.base.showSimpleDialog
import com.core.wumfapp2020.databinding.FrgHomeBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.viewmodel.HomeViewModel
import com.core.wumfapp2020.viewmodel.IN_ANOTHER_COUNTRY
import com.core.wumfapp2020.viewmodel.IN_MY_COUNTRY
import com.library.core.di.lazyViewModel

class HomeFragment : AppBaseFragment<FrgHomeBinding, HomeViewModel>(R.layout.frg_home) {

    override val viewModel by lazyViewModel { injector.homeViewModel }

    override val bottomTabs = VisibleBottomTabsState(0)

    override fun setViewModelInBinding(binding: FrgHomeBinding, viewModel: HomeViewModel) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appsRecycleView.showLoadedState()
        observeEvent(viewModel.showPickAppCategoryDialog) {
            showPickAppCategoryDialog()
        }
        observeEvent(viewModel.showCountriesDialog) {
            showCountryDialog(it)
        }
        observeEvent(viewModel.showPickedApps) {
            binding.appsRecycleView.setPackages(it.appPackages, it.likes)
        }
    }

    private fun showPickAppCategoryDialog() { //in the world; in country; among friends
        showSimpleDialog(requireContext(), R.array.type_of_apps, -1,
            { array, pos ->
                val country: Country? = if (pos == IN_MY_COUNTRY) viewModel.getDefaultCountry()
                else null
                viewModel.pickedTypeOfApps(pos, country)
            }, {
                it.dismiss()
            }, viewModel.getDefaultCountryName())
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