package com.core.dynamicfeature.viewmodel

import android.util.Log
import com.core.dynamicfeature.Obj2T
import com.core.dynamicfeature.fragment.OnBoardingFragmentArgs
import com.core.wumfapp2020.ResultListener
import com.core.wumfapp2020.ResultStatus
import com.core.wumfapp2020.testdi.Obj1T
import com.library.core.BaseViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import javax.inject.Inject


class OnBoardingViewModel @AssistedInject constructor(@Assisted private val someArg: ResultListener?, obj2T: Obj2T, obj1T: Obj1T): BaseViewModel()  {

    init {
        Log.i("testr", "resultListener=" + someArg.hashCode())
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(someArg: ResultListener?): OnBoardingViewModel
    }

    fun onClick() {
        someArg?.status?.postValue(ResultStatus.SUCCESS)
    }

    override fun handleException(e: Throwable) {
        TODO("Not yet implemented")
    }

}