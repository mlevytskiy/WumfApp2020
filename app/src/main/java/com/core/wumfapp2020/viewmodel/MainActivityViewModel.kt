package com.core.wumfapp2020.viewmodel

import androidx.databinding.ObservableBoolean
import com.core.wumfapp2020.memory.UserInfoRepository
import com.library.core.BaseViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val memory: UserInfoRepository): BaseViewModel() {

    val showBottomTabs = ObservableBoolean(false)

    override fun handleException(e: Throwable) {
        TODO("Not yet implemented")
    }

    fun isSkipOnBoarding(): Boolean {
        return !memory.getToken().isNullOrEmpty()
    }

}