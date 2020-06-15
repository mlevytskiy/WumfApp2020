package com.core.wumfapp2020.viewmodel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.core.wumfapp2020.InternetConnectionChecker
import com.core.wumfapp2020.fragment.PreOnBoardingFragmentDirections
import com.core.wumfapp2020.memory.UserInfoRepository
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.library.core.BaseViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

class PreOnBoardingViewModel @Inject constructor(private val connectionChecker: InternetConnectionChecker, private val manager: SplitInstallManager,
                                                 val sharedViewModel: SharedViewModel, userInfoRepository: UserInfoRepository, private val repository: UserInfoRepository): BaseViewModel() {

    private val directions = PreOnBoardingFragmentDirections.Companion

    val inProgress = ObservableBoolean(false)

    enum class ConnectionCheckingState {
        NO_INTERNET,
        CHECKING,
        SHOW_MESSAGE_CONNECTED,
        CONNECTED
    }
    val internetConnectionState = ObservableField(ConnectionCheckingState.CONNECTED)

    init {
        Log.i("testr", "token=" + userInfoRepository.getToken())
    }

    override fun handleException(e: Throwable) {

    }

    fun signInAsAnonymous() {
        if (!repository.isPhoneNumberHintShowed()) {
            navigate(directions.actionPreOnBoardingToDetectingYourPhoneNumber())
        } else {
            navigate(directions.actionPreOnBoardingToEnterPhoneNumber())
        }
    }

    fun handleOnBoardingResult(isOnboardingPassed: Boolean) {
        Log.i("testr", "handleOnBoardingResult " + "hashcode=" + this.hashCode())
        toast("1=" + isOnboardingPassed)
    }

    fun signInWithTelegram() {
        if (!checkInternetConnection()) return
        inProgress.set(true)
        startBgJob {
            delay(2000)
            inProgress.set(false)
//            startDownloadModule()
        }

    }

//    private fun startDownloadModule() {
//        val request = SplitInstallRequest.newBuilder()
//            .addModule("onboarding")
//            .build()
//        manager.registerListener {
//            when (it.status()) {
//                SplitInstallSessionStatus.DOWNLOADING -> showToast("Downloading feature")
//                SplitInstallSessionStatus.INSTALLED -> {
//                    showToast("Feature ready to be used")
//                }
//                else -> { showToast("${it.status()}") }
//            }
//        }
//        manager.startInstall(request).addOnSuccessListener {
//            postEvent(OpenOnBoarding())
//        }.addOnFailureListener {
//            val sw = StringWriter()
//            it.printStackTrace(PrintWriter(sw))
//            val exceptionAsString: String = sw.toString()
//            showToast("failed:" + exceptionAsString)
//        }
//    }

    fun checkInternetConnection(): Boolean {
        val result = connectionChecker.hasInternetConnection()
        internetConnectionState.set(if (connectionChecker.hasInternetConnection()) ConnectionCheckingState.CONNECTED else ConnectionCheckingState.NO_INTERNET)
        return result
    }

    fun onClickRefresh() {
        if (internetConnectionState.get() != ConnectionCheckingState.CHECKING) {
            internetConnectionState.set(ConnectionCheckingState.CHECKING)
            startBgJob {
                delay(1500)
                if (connectionChecker.hasInternetConnection()) {
                    internetConnectionState.set(ConnectionCheckingState.SHOW_MESSAGE_CONNECTED)
                    delay(1000)
                    internetConnectionState.set(ConnectionCheckingState.CONNECTED)
                } else {
                    internetConnectionState.set(ConnectionCheckingState.NO_INTERNET)
                }
            }
        }
    }
}