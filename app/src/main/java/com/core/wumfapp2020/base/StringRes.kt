package com.core.wumfapp2020.base

import android.content.res.Resources

class StringRes(private val res: Resources) {

    fun getStr(strId: Int) = res.getString(strId)

    fun getStrFromArray(arrayStrId: Int, position: Int) = res.getStringArray(arrayStrId)[position]

}