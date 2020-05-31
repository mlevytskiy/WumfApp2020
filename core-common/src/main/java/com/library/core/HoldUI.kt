package com.library.core

import androidx.databinding.ViewDataBinding
import java.lang.ref.WeakReference

interface HoldUI<B : ViewDataBinding> {

    fun setUI(binding: B?)

    fun getUI(): WeakReference<B>?

    fun needHoldUI(): Boolean

}