package com.core.wumfapp2020.viewmodel

import androidx.databinding.ObservableBoolean
import com.app.api.api.AddAppRequest
import com.app.api.api.HeaderInterceptor
import com.app.api.api.RemoveAppRequest
import com.app.api.api.WumfApi
import com.core.wumfapp2020.memory.MyAppsCollectionRepository
import com.core.wumfapp2020.memory.UserInfoRepository
import com.library.core.BaseViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val userInfoRepository: UserInfoRepository,
        private val myAppsCollectionRepository: MyAppsCollectionRepository, private val wumfApi: WumfApi,
        private val headerInterceptor: HeaderInterceptor): BaseViewModel() {

    val showBottomTabs = ObservableBoolean(false)

    override fun handleException(e: Throwable) {
        TODO("Not yet implemented")
    }

    fun isSkipOnBoarding(): Boolean {
        return !userInfoRepository.getToken().isNullOrEmpty()
    }

    fun updateToken() {
        userInfoRepository.getToken()?.let {
            headerInterceptor.updateToken(it)
        }
    }

    fun syncMyCollection() {
        startBgJob {
            var removeAppsCompleted: Boolean = false
            myAppsCollectionRepository.getDeferredTaskRemoveApps().forEach {
                removeAppsCompleted = wumfApi.removeApp(RemoveAppRequest(it)).execute().body()?.apps?.contains(it) != true
            }
            if (removeAppsCompleted) {
                myAppsCollectionRepository.clearDeferredTaskRemoveApps()
            }
            var addAppsCompleted = false
            myAppsCollectionRepository.getDeferredTaskAddApps().forEach {
                addAppsCompleted = wumfApi.addApp(AddAppRequest(it)).execute().body()?.apps?.contains(it) == true
            }
            if (addAppsCompleted) {
                myAppsCollectionRepository.clearDeferredTaskAddApps()
            }
        }
    }

}