package com.core.wumfapp2020.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
//import com.core.wumfapp2020.databinding.FrgSample1Binding
import com.library.core.BaseViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import java.lang.Exception
import javax.inject.Inject


//@Module
//class SampleModule1 {
//    @Provides
//    @IntoMap
//    @ViewModelKey(SampleViewModel1::class)
//    fun bindViewModelKey(): ViewModel = SampleViewModel1()
//}
//
//class SampleViewModel1 @Inject constructor(): BaseViewModel<FrgSample1Binding>() {
//
//    val str = "fragment 1"
//
//    constructor(parcel: Parcel) : this() {
//    }
//
//    fun handleTestError() {
//        handleException(Exception("Some error"))
//    }
//
//    fun nextScreen() {
//        navigate(SampleFragment1Directions.actionSample1ToSample2())
//    }
//
//    fun prevScreen() {
//        //do nothing
//    }
//
//    override fun handleException(e: Throwable) {
//
//    }
//
//    companion object CREATOR : Parcelable.Creator<SampleViewModel1> {
//        override fun createFromParcel(parcel: Parcel): SampleViewModel1 {
//            return SampleViewModel1(parcel)
//        }
//
//        override fun newArray(size: Int): Array<SampleViewModel1?> {
//            return arrayOfNulls(size)
//        }
//    }
//
//}