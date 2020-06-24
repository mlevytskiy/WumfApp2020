package com.core.wumfapp2020.viewmodel

import com.core.wumfapp2020.InternetConnectionChecker
import com.core.wumfapp2020.memory.UserInfoRepository
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.library.core.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val connectionChecker: InternetConnectionChecker, private val manager: SplitInstallManager,
                                        val sharedViewModel: SharedViewModel, userInfoRepository: UserInfoRepository): BaseViewModel() {

    //    private val directions = PreOnBoardingFragmentDirections.Companion

    override fun handleException(e: Throwable) {

    }

}