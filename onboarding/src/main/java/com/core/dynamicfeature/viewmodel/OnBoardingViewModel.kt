package com.core.dynamicfeature.viewmodel

import android.util.Log
import com.core.dynamicfeature.Obj2T
//import com.core.dynamicfeature.fragment.DetectingYourPhoneNumberFragmentDirections
import com.core.wumfapp2020.memory.UserInfoRepository
import com.core.wumfapp2020.viewmodel.AnyFragmentBaseViewModel
import com.core.wumfapp2020.viewmodel.ResultStatus
import com.core.wumfapp2020.viewmodel.SharedViewModel
import com.squareup.inject.assisted.AssistedInject


class OnBoardingViewModel @AssistedInject constructor(obj2T: Obj2T, var sharedViewModel: SharedViewModel, userInfoRepository: UserInfoRepository): AnyFragmentBaseViewModel()  {

//    private val directions = OnBoardingViewModelFragmentDirections.Companion

    init {
        Log.i("testr", "token2=" + userInfoRepository.getToken())
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(): OnBoardingViewModel
    }

    fun onClick() {
        sharedViewModel.status = ResultStatus.SUCCESS
    }

}