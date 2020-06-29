package com.core.wumfapp2020.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import com.app.api.api.*
import com.core.wumfapp2020.InternetConnectionChecker
import com.core.wumfapp2020.base.ColorRes
import com.core.wumfapp2020.base.StringRes
import com.core.wumfapp2020.base.countriesdialog.CountriesHolder
import com.core.wumfapp2020.base.countriesdialog.Country
import com.core.wumfapp2020.memory.UserInfoRepository
import com.core.wumfapp2020.viewmodel.home.HomeTitle
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.library.core.BaseViewModel
import com.library.core.SingleLiveEvent
import retrofit2.Call
import javax.inject.Inject

private const val IN_THE_WORLD = 0
const val IN_MY_COUNTRY = 1
const val IN_ANOTHER_COUNTRY = 2
private const val AMONG_FRIENDS = 3

class HomeViewModel @Inject constructor(private val connectionChecker: InternetConnectionChecker, private val manager: SplitInstallManager,
                                        val sharedViewModel: SharedViewModel, userInfoRepository: UserInfoRepository,
                                        stringRes: StringRes, colorRes: ColorRes, private val countryHolder: CountriesHolder,
                                        private val wumfApi: WumfApi): BaseViewModel() {

    //    private val directions = PreOnBoardingFragmentDirections.Companion

    private val showPickAppCategoryDialogMutable = SingleLiveEvent<HomeTitle.Type>()
    val showPickAppCategoryDialog: LiveData<HomeTitle.Type> = showPickAppCategoryDialogMutable

    class PickedApps(val appPackages: String, val likes: Map<String, List<Int>>)
    private val showPickedAppsMutable = SingleLiveEvent<PickedApps>()
    val showPickedApps: LiveData<PickedApps> = showPickedAppsMutable

    private val showCountriesDialogMutable = SingleLiveEvent<CountriesHolder>()
    val showCountriesDialog: LiveData<CountriesHolder> = showCountriesDialogMutable

    val span = HomeTitle(stringRes, colorRes)

    override fun handleException(e: Throwable) {

    }

    fun showPickTypeOfAppsDialog() {
        showPickAppCategoryDialogMutable.postEvent(span.type)
    }

    fun pickedTypeOfApps(position: Int, country: Country?) {
        when (position) {
            IN_THE_WORLD -> {
                span.type = HomeTitle.Type.IN_THE_WORLD
                getAllWorldApps()
            }
            IN_MY_COUNTRY -> {
                val code = countryHolder.getDeviceCountryCode()
                span.countryName = getDefaultCountryName(code)
                getCountryApps(code)
                if (span.type == HomeTitle.Type.IN_COUNTRY) {
                    span.forciblyTextUpdate()
                } else {
                    span.type = HomeTitle.Type.IN_COUNTRY
                }
            }
            IN_ANOTHER_COUNTRY -> {
                if (country == null) {
                    showCountriesDialogMutable.postEvent(countryHolder)
                    return
                }
                span.countryName = country.name
                getCountryApps(country.code)
                if (span.type == HomeTitle.Type.IN_COUNTRY) {
                    span.forciblyTextUpdate()
                } else {
                    span.type = HomeTitle.Type.IN_COUNTRY
                }
            }
            AMONG_FRIENDS -> {
                span.type = HomeTitle.Type.AMONG_FRIENDS
            }
        }
    }

    fun getAllWorldApps() {
        startBgJob {
            Log.i("testr", "thread=" + Thread.currentThread())
            callRetrofit(
                call = wumfApi.getNotMyApps(GetNotMyAppsRequest(allWorld = true, friends= emptyList())),
                result = { response ->
                    fillApps(response)
                    "appsIsNotEmpty=${response?.apps?.isNotEmpty()}"
                }
            )
        }
    }

    fun getDefaultCountryName(): String {
        val code = countryHolder.getDeviceCountryCode()
        return getDefaultCountryName(code)
    }

    fun getDefaultCountry(): Country? {
        val code = countryHolder.getDeviceCountryCode()
        return getDefaultCountry(code)
    }

    private fun getDefaultCountryName(code: String): String {
        val country = getDefaultCountry(code)
        return country?.name ?: ""
    }

    private fun getDefaultCountry(code: String): Country? {
        val country = countryHolder.countries.find {
            it.code == code
        }
        return country
    }

    private fun <T> callRetrofit(call: Call<T>, result: (T?)->(String)) {
        val response = executeRetrofit(call=call,
            generalError = {
                    e -> toast("error=" + e.message)
                Log.e("testr", "error=", e)
            })
        response?.let{
            toast(result.invoke(it))
        }
    }

    fun getCountryApps(country: String) {
        startBgJob {
            callRetrofit(
                call = wumfApi.getNotMyApps(
                    GetNotMyAppsRequest(
                        inCountry = true,
                        country = country,
                        friends = emptyList()
                    )
                ),
                result = { response ->
                    fillApps(response)
                    "appsIsNotEmpty=${response?.apps?.isNotEmpty()}"
                }
            )
        }
    }

    private fun fillApps(response: GetNotMyAppsResponse?) {
        response?.let {
            val appsStr = prepareAppsForAdapter(it.apps)
            val likes = prepareLikesForAdapter(it.apps)
            showPickedAppsMutable.postEvent(PickedApps(appsStr, likes))
        } ?: kotlin.run {
            toast("response is null")
        }
    }

    private fun prepareAppsForAdapter(apps: List<App>): String {
        if (apps.isEmpty()) {
            return ""
        } else {
            return apps.map { it.packageName }.joinToString(",")
        }
    }

    private fun prepareLikesForAdapter(apps: List<App>): Map<String, List<Int>> {
        val likes = HashMap<String, List<Int>>()
        apps.forEach {
            likes.put(it.packageName, it.whoLikes)
        }
        return likes
    }

}