package com.core.dynamicfeature.viewmodel

import com.core.wumfapp2020.memory.UserInfoRepository
import com.core.wumfapp2020.viewmodel.SharedViewModel
import com.library.core.BaseViewModel
import com.squareup.inject.assisted.AssistedInject

class EnterPhoneNumberViewModel @AssistedInject constructor(var sharedViewModel: SharedViewModel, userInfoRepository: UserInfoRepository): BaseViewModel() {

//    private val directions = EnterPhoneNumberFragmentDirections.Companion

    @AssistedInject.Factory
    interface Factory {
        fun create(): EnterPhoneNumberViewModel
    }

    override fun handleException(e: Throwable) {
        TODO("Not yet implemented")
    }
}