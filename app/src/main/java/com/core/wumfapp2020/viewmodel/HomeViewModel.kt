package com.core.wumfapp2020.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.api.api.*
import com.core.wumfapp2020.base.ColorRes
import com.core.wumfapp2020.base.StringRes
import com.core.wumfapp2020.base.countriesdialog.CountriesHolder
import com.core.wumfapp2020.fragment.HomeFragmentDirections
import com.core.wumfapp2020.memory.HomeState
import com.core.wumfapp2020.memory.HomeStateRepository
import com.core.wumfapp2020.memory.UserInfoRepository
import com.core.wumfapp2020.memory.impl.*
import com.core.wumfapp2020.viewmodel.home.HomeTitle
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.library.core.SingleLiveEvent
import com.library.core.log
import com.squareup.inject.assisted.AssistedInject
import krafts.alex.tg.TgClient
import retrofit2.Call
import wumf.com.detectphone.AppCountryDetector
import wumf.com.detectphone.Country

const val IN_THE_WORLD = 0
const val IN_MY_COUNTRY = 1
const val IN_ANOTHER_COUNTRY = 2
const val AMONG_FRIENDS = 3

class HomeViewModel @AssistedInject constructor(private val homeStateRepository: HomeStateRepository, private val manager: SplitInstallManager,
                                                val sharedViewModel: SharedViewModel, val memory: UserInfoRepository,
                                                stringRes: StringRes, colorRes: ColorRes, private val countryHolder: CountriesHolder,
                                                private val wumfApi: WumfApi, private val tdClient: TgClient
): AnyFragmentBaseViewModel() {

    private val directions = HomeFragmentDirections.Companion

    private val showPickAppCategoryDialogMutable = SingleLiveEvent<HomeTitle.Type>()
    val showPickAppCategoryDialog: LiveData<HomeTitle.Type> = showPickAppCategoryDialogMutable

    class PickedApps(val appPackages: List<String>, val likes: Map<String, List<Int>>)
    private val showPickedAppsMutable = MutableLiveData<PickedApps>().apply {
        if (!homeStateRepository.isEmpty()) {
            homeStateRepository.current()?.let {
                this.value = PickedApps(it.apps.map { app->app.packageName }, it.apps.associate { app-> app.packageName to app.whoLikes })
            }
        }
    }
    val showPickedApps: LiveData<PickedApps> = showPickedAppsMutable

    private val showCountriesDialogMutable = SingleLiveEvent<CountriesHolder>()
    val showCountriesDialog: LiveData<CountriesHolder> = showCountriesDialogMutable

    val span = HomeTitle(stringRes, colorRes, getHomeTitleType(homeStateRepository.current()), getCountry(homeStateRepository.current()))

    init {
        "HomeViewModel init ${this.hashCode()} ${viewModelScope.hashCode()}".log()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(): HomeViewModel
    }

    private fun getHomeTitleType(state: com.core.wumfapp2020.memory.HomeState?): HomeTitle.Type {
        if (state == null) {
            return HomeTitle.Type.IN_THE_WORLD
        }
        return when(state.appsSource.type) {
            TYPE_IN_THE_WORLD -> HomeTitle.Type.IN_THE_WORLD
            TYPE_IN_ANOTHER_COUNTRY -> HomeTitle.Type.IN_ANOTHER_COUNTRY
            TYPE_IN_MY_COUNTRY -> HomeTitle.Type.IN_MY_COUNTRY
            TYPE_AMONG_FRIENDS -> HomeTitle.Type.AMONG_FRIENDS
            else -> HomeTitle.Type.IN_THE_WORLD
        }
    }

    private fun getCountry(state: HomeState?): Country? {
        val country =  AppCountryDetector.detectCountryByPhoneCode(state?.appsSource?.countryMCC)
        country?.name = state?.appsSource?.countryName ?: ""
        return country
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
                val defaultCountry = getDefaultCountry()
                defaultCountry?.let {
                    span.country = it
                    getCountryApps(it.code)
                }
                span.type = HomeTitle.Type.IN_MY_COUNTRY
                span.forciblyTextUpdate()
            }
            IN_ANOTHER_COUNTRY -> {
                if (country == null) {
                    showCountriesDialogMutable.postEvent(countryHolder)
                    return
                }
                span.country = country
                getCountryApps(country.code)
                span.type = HomeTitle.Type.IN_ANOTHER_COUNTRY
                span.forciblyTextUpdate()
            }
            AMONG_FRIENDS -> {
                span.type = HomeTitle.Type.AMONG_FRIENDS
                getAppsAmongFriends()
            }
        }
    }

    fun loadDataIfMemoryEmpty() {
        if (homeStateRepository.isEmpty()) {
            loadData()
        }
    }

    fun loadData() {
        when(span.type) {
            HomeTitle.Type.AMONG_FRIENDS -> getAppsAmongFriends()
            HomeTitle.Type.IN_ANOTHER_COUNTRY,
            HomeTitle.Type.IN_MY_COUNTRY -> {
                getDefaultCountry()?.let {
                    getCountryApps(it.code)
                }
            }
            HomeTitle.Type.IN_THE_WORLD -> getAllWorldApps()
        }
    }

    fun getAllWorldApps() {
         startBgJob {
            "startBgJob getAllWorldApps()".log()
            callRetrofit(
                call = wumfApi.getNotMyApps(GetNotMyAppsRequest(allWorld = true, friends= emptyList())),
                result = { response ->
                    fillApps(response)
                    "appsIsNotEmpty=${response?.apps?.isNotEmpty()}"
                }
            )
        }
    }

    fun getAppsAmongFriends() {
        startBgJob {
            val friends = tdClient.getContacts().userIds.map { it }
            callRetrofit(
                call = wumfApi.getNotMyApps(GetNotMyAppsRequest(amongFriends = true, friends=friends)),
                result = { response ->
                    fillApps(response)
                    "appsIsNotEmpty=${response?.apps?.isNotEmpty()}"
                }
            )
        }
    }

    fun navigateToPeopleWhoLikes() {
        navigate(directions.actionHomeToPeopleWhoLikes())
    }

    fun getDefaultCountry(): Country? {
        val mcc = memory.getCountryMCC()
        return AppCountryDetector.detectCountryByPhoneCode(mcc)
    }

    private fun <T> callRetrofit(call: Call<T>, result: (T?)->(String)) {
        val response = executeRetrofit(call=call,
            generalError = {
                    e -> toast("error=" + e.message)
                Log.e("testr", "error=", e)
            })
        response?.let{
            result.invoke(it)
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
        "fill apps apps=${response?.apps?.size}".log()
        response?.let {
            val likes = prepareLikesForAdapter(it.apps)
            homeStateRepository.update(createHomeState(it.apps, span.type, span.country?.mcc?:0, span.country?.name?:""))
            showPickedAppsMutable.postEvent(PickedApps(it.apps.map {app->app.packageName }, likes))
        } ?: kotlin.run {
            toast("response is null")
        }
    }

    private fun createHomeState(apps: List<App>, type: HomeTitle.Type, countryMMI: Int, countryName: String): HomeState {
        val state = HomeState()
        state.apps = apps.map { app-> com.core.wumfapp2020.memory.App(app.packageName, app.whoLikes) }.toMutableList()
        val typeInt = when(type) {
            HomeTitle.Type.IN_MY_COUNTRY-> TYPE_IN_MY_COUNTRY
            HomeTitle.Type.IN_ANOTHER_COUNTRY-> TYPE_IN_ANOTHER_COUNTRY
            HomeTitle.Type.IN_THE_WORLD-> TYPE_IN_THE_WORLD
            else -> TYPE_AMONG_FRIENDS
        }
        state.appsSource = HomeAppsSource(typeInt, countryMMI, countryName)
        return state
    }

    private fun prepareLikesForAdapter(apps: List<App>): Map<String, List<Int>> {
        val likes = HashMap<String, List<Int>>()
        apps.forEach {
            likes.put(it.packageName, it.whoLikes)
        }
        return likes
    }

}