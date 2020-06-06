package com.core.dynamicfeature.viewmodel

import android.util.Log
import com.core.dynamicfeature.Obj2T
import com.core.wumfapp2020.testdi.Obj1T
import com.library.core.BaseViewModel
import javax.inject.Inject

class OnBoardingViewModel @Inject constructor(obj2T: Obj2T, obj1T: Obj1T): BaseViewModel()  {

    init {
        Log.i("testr", "init OnBoardingViewModel")
        obj1T.test()
        obj2T.test()
    }

    override fun handleException(e: Throwable) {
        TODO("Not yet implemented")
    }

}