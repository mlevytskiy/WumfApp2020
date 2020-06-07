package com.core.dynamicfeature.viewmodel

import android.util.Log
import com.core.dynamicfeature.Obj2T
import com.core.dynamicfeature.fragment.OnBoardingFragmentArgs
import com.core.wumfapp2020.testdi.Obj1T
import com.library.core.BaseViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import javax.inject.Inject


class OnBoardingViewModel @AssistedInject constructor(@Assisted someArg: String?, obj2T: Obj2T, obj1T: Obj1T): BaseViewModel()  {

    init {
        Log.i("testr", "init OnBoardingViewModel")
        Log.i("testr", "print arg=" + someArg)
        obj1T.test()
        obj2T.test()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(someArg: String?): OnBoardingViewModel
    }

    override fun handleException(e: Throwable) {
        TODO("Not yet implemented")
    }

}