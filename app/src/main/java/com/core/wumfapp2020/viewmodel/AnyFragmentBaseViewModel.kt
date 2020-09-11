package com.core.wumfapp2020.viewmodel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.core.wumfapp2020.InternetConnectionChecker
import com.core.wumfapp2020.viewmodel.home.HomeTitle
import com.library.Event
import com.library.core.BaseViewModel
import com.library.core.SingleLiveEvent
import kotlinx.coroutines.*
import java.io.IOException
import javax.inject.Inject

abstract class AnyFragmentBaseViewModel: BaseViewModel() {

    @Inject
    @JvmField
    var connectionChecker: InternetConnectionChecker? = null

    private val sendInternetStatusMutable = SingleLiveEvent<Event<Boolean>>()
    val sendInternetStatus: LiveData<Event<Boolean>> = sendInternetStatusMutable

    private val showErrorMessageMutable = SingleLiveEvent<Event<String>>()
    val showErrorMessage: LiveData<Event<String>> = showErrorMessageMutable

    fun asyncCall(errorHandler: (e: Throwable)->Unit = this::handleError,
                  ifBlockNotCalled: () -> Unit = { },
                  block: suspend CoroutineScope.() -> Unit): Job {
        val scope = viewModelScope onError {
            ifBlockNotCalled()
            errorHandler(it)
        }
        return startBgJob(scope = scope) {
            val hasInternet = connectionChecker?.hasInternetConnection() == true
            sendInternetStatusMutable.postEvent(Event(hasInternet))
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