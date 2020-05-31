package com.core.wumfapp2020.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.core.wumfapp2020.InternetConnectionChecker
import com.core.wumfapp2020.databinding.FrgPreOnBoardingBinding
import com.core.wumfapp2020.event.OpenOnBoarding
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.library.core.BaseViewModel
import com.library.core.di.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.delay
import java.io.PrintWriter
import java.io.StringWriter
import javax.inject.Inject

@Module
class PreOnBoardingViewModule {
    @Provides
    @IntoMap
    @ViewModelKey(PreOnBoardingViewModel::class)
    fun bindViewModelKey(connectionChecker: InternetConnectionChecker, splitInstallManager: SplitInstallManager): ViewModel = PreOnBoardingViewModel(connectionChecker, splitInstallManager)
}

class PreOnBoardingViewModel @Inject constructor(private val connectionChecker: InternetConnectionChecker, private val manager: SplitInstallManager): BaseViewModel<FrgPreOnBoardingBinding>() {

    val inProgress = ObservableBoolean(false)

    enum class ConnectionCheckingState {
        NO_INTERNET,
        CHECKING,
        SHOW_MESSAGE_CONNECTED,
        CONNECTED
    }
    val internetConnectionState = ObservableField<ConnectionCheckingState>(ConnectionCheckingState.CONNECTED)

    override fun handleException(e: Throwable) {

    }

    fun signInAsAnonymous() {
        if (!checkInternetConnection()) return
    }

    fun signInWithTelegram() {
        if (!checkInternetConnection()) return
        inProgress.set(true)
        startBgJob {
            delay(2000)
            inProgress.set(false)
            startDownloadModule()
        }

    }

    private fun startDownloadModule() {
        val request = SplitInstallRequest.newBuilder()
            .addModule("onboarding")
            .build()
        manager.registerListener {
            when (it.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> showToast("Downloading feature")
                SplitInstallSessionStatus.INSTALLED -> {
                    showToast("Feature ready to be used")
                }
                else -> { showToast("${it.status()}") }
            }
        }
        manager.startInstall(request).addOnSuccessListener {
            postEvent(OpenOnBoarding())
        }.addOnFailureListener {
            val sw = StringWriter()
            it.printStackTrace(PrintWriter(sw))
            val exceptionAsString: String = sw.toString()
            showToast("failed:" + exceptionAsString)
        }
    }

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