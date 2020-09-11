package com.core.wumfapp2020.viewmodel

import androidx.databinding.ObservableBoolean
import com.core.wumfapp2020.memory.UserInfoRepository
import com.google.android.play.core.splitinstall.SplitInstallManager
import javax.inject.Inject

class MoreViewModel @Inject constructor(private val manager: SplitInstallManager,
                                        val sharedViewModel: SharedViewModel, userInfoRepository: UserInfoRepository, private val repository: UserInfoRepository): AnyFragmentBaseViewModel() {

//    private val directions = PreOnBoardingFragmentDirections.Companion

    val inProgress = ObservableBoolean(false)

    init {
    }




}