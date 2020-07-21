package com.core.wumfapp2020.viewmodel

import androidx.databinding.ObservableBoolean
import com.core.wumfapp2020.InternetConnectionChecker
import com.core.wumfapp2020.memory.MyAppsCollectionRepository
import com.core.wumfapp2020.memory.UserInfoRepository
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.library.core.BaseViewModel
import javax.inject.Inject

class AddAppInMyCollectionViewModel @Inject constructor(private val connectionChecker: InternetConnectionChecker, private val manager: SplitInstallManager,
                                                        val sharedViewModel: SharedViewModel, userInfoRepository: UserInfoRepository,
                                                        private val appsRepository: MyAppsCollectionRepository): BaseViewModel() {

//    private val directions = PreOnBoardingFragmentDirections.Companion

    val inProgress = ObservableBoolean(false)

    init {
    }

    override fun handleException(e: Throwable) {

    }

    fun onClickAddApp() {
        
    }

    fun addAppToMyCollection(pkgName: String) {
        appsRepository.addToMyApps(pkgName)
    }

    fun moveToMyCollectionScreen() {
        popBack()
    }


}