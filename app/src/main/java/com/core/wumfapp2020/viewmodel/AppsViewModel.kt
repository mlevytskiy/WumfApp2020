package com.core.wumfapp2020.viewmodel

import androidx.databinding.ObservableBoolean
import com.core.wumfapp2020.InternetConnectionChecker
import com.core.wumfapp2020.fragment.AppsFragmentDirections
import com.core.wumfapp2020.memory.UserInfoRepository
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.library.core.BaseViewModel
import javax.inject.Inject

class AppsViewModel @Inject constructor(private val connectionChecker: InternetConnectionChecker, private val manager: SplitInstallManager,
                                        val sharedViewModel: SharedViewModel, userInfoRepository: UserInfoRepository, private val repository: UserInfoRepository): BaseViewModel() {

    private val directions = AppsFragmentDirections.Companion

    val inProgress = ObservableBoolean(false)

    init {
    }

    override fun handleException(e: Throwable) {

    }

    fun onClickAddApp() {
        navigate(directions.actionAppsToAddAppInMyCollection())
    }

}