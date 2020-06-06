package com.core.wumfapp2020.di

import android.app.Activity
import androidx.fragment.app.Fragment

interface DaggerComponentProvider {
    val component: AppComponent
}
val Activity.injector get() = (application as DaggerComponentProvider).component

val Fragment.injector get() = (requireActivity().application as DaggerComponentProvider).component


