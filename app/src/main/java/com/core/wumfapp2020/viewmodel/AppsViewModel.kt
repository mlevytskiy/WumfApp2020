package com.core.wumfapp2020.viewmodel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import com.app.api.api.App
import com.core.wumfapp2020.InternetConnectionChecker
import com.core.wumfapp2020.fragment.AppsFragmentDirections
import com.core.wumfapp2020.memory.MyAppsCollectionRepository
import com.core.wumfapp2020.memory.UserInfoRepository
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.library.core.BaseViewModel
import com.library.core.SingleLiveEvent
import javax.inject.Inject

class AppsViewModel @Inject constructor(private val connectionChecker: InternetConnectionChecker, private val manager: SplitInstallManager,
                                        val sharedViewModel: SharedViewModel, userInfoRepository: UserInfoRepository,
                                        private val appsRepository: MyAppsCollectionRepository): BaseViewModel() {

    private val directions = AppsFragmentDirections.Companion

    val inProgress = ObservableBoolean(false)
    val isMyCollectionEmpty = ObservableBoolean(true)

    class PickedApps(val appPackages: String, val likes: Map<String, List<Int>>)
    private val showPickedAppsMutable = SingleLiveEvent<PickedApps>()
    val showPickedApps: LiveData<PickedApps> = showPickedAppsMutable

    init {
    }

    override fun handleException(e: Throwable) {

    }

    fun updateData() {
        val isEmpty = appsRepository.isEmpty()
        isMyCollectionEmpty.set(isEmpty)
        if (!isEmpty) {
            val appsStr = prepareAppsForAdapter(appsRepository.getMyApps())
            showPickedAppsMutable.postEvent(PickedApps(appsStr, emptyMap()))
        }
    }

    fun removeAppFromMemory(pkg: String) {
        val isRemoved = appsRepository.removeFromMyApps(pkg)
        if (isRemoved) {
            updateData()
        }
    }

    fun printApps() {
        Log.i("testr", "apps count in memory=${appsRepository.getMyApps().size}")
    }

    fun onClickAddApp() {
        navigate(directions.actionAppsToAddAppInMyCollection())
    }

    private fun prepareAppsForAdapter(apps: List<String>): String {
        return when {
            apps.isEmpty() -> ""
            else -> apps.joinToString(",")
        }
    }

}