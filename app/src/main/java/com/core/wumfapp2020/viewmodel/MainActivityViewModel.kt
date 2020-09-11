package com.core.wumfapp2020.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.api.api.AddAppRequest
import com.app.api.api.HeaderInterceptor
import com.app.api.api.RemoveAppRequest
import com.app.api.api.WumfApi
import com.core.wumfapp2020.InternetConnectionChecker
import com.core.wumfapp2020.memory.MyAppsCollectionRepository
import com.core.wumfapp2020.memory.UserInfoRepository
import com.library.Event
import com.library.core.BaseViewModel
import com.library.core.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val userInfoRepository: UserInfoRepository,
        private val myAppsCollectionRepository: MyAppsCollectionRepository, private val wumfApi: WumfApi,
        private val headerInterceptor: HeaderInterceptor, private val connectionChecker: InternetConnectionChecker): BaseViewModel() {

    val showBottomTabs = ObservableBoolean(false)
    val hasInternetConnection = ObservableBoolean(true)

    private val showErrorMessageMutable = SingleLiveEvent<Event<String>>()
    val showErrorMessage: LiveData<Event<String>> = showErrorMessageMutable

    fun isSkipOnBoarding(): Boolean {
        return !userInfoRepository.getToken().isNullOrEmpty()
    }

    fun updateToken() {
        userInfoRepository.getToken()?.let {
            headerInterceptor.updateToken(it)
        }
    }

    fun onClickCheckInternet() {
        progress.set(true)
        startBgJob {
            delay(2000)
            progress.set(false)
            delay(300)
            hasInternetConnection.set(connectionChecker.hasInternetConnection())
        }
    }

    fun onClickHideNoInternetConnectionMessage() {
        hasInternetConnection.set(true)
    }

    fun syncMyCollection() {
        asyncCall {
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

    fun asyncCall(errorHandler: (e: Throwable)->Unit = this::handleError,
                  ifBlockNotCalled: () -> Unit = { },
                  block: suspend CoroutineScope.() -> Unit): Job {
        val scope = viewModelScope onError {
            ifBlockNotCalled()
            errorHandler(it)
        }
        return startBgJob(scope = scope) {
            val hasInternet = connectionChecker.hasInternetConnection()
            hasInternetConnection.set(hasInternet)
            if (hasInternet) {
                block()
            } else {
                ifBlockNotCalled()
            }
        }
    }

    fun handleError(e: Throwable) {
        showErrorMessageMutable.postEvent(Event(e.message.toString()))
    }

}