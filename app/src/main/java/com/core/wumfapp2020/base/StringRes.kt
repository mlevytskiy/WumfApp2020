package com.core.wumfapp2020.base

import android.content.res.Resources

class StringRes(private val res: Resources) {

    fun getStr(strId: Int) = res.getString(strId)

    fun getStrFormat(strId: Int, field: String) = String.format(res.getString(strId), field)

    fun getStrFromArray(arrayStrId: Int, position: Int) = res.getStringArray(arrayStrId)[position]

    fun getPlurals(id: Int, value: Int): String {
        return res.getQuantityString(id, value, value)
    }

}