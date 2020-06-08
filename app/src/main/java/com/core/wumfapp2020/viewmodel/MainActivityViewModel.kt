package com.core.wumfapp2020.viewmodel

import androidx.databinding.ObservableBoolean
import com.library.core.BaseViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(): BaseViewModel() {

    val showBottomTabs = ObservableBoolean(false)

    override fun handleException(e: Throwable) {
        TODO("Not yet implemented")
    }

}