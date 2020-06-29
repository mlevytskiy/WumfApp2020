package com.core.core_adapters

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import wumf.com.appsprovider.App
import wumf.com.appsprovider.AppProvider
import wumf.com.appsprovider.OnChangeLastInstalledAppsListener
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (this.hasFocus()) {
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    } else {
        this.rootView.findFocus()?.let { imm.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT) }
    }
}