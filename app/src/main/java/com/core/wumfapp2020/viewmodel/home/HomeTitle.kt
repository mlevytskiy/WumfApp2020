package com.core.wumfapp2020.viewmodel.home

import android.text.Spannable
import androidx.databinding.ObservableField
import com.core.wumfapp2020.R
import com.core.wumfapp2020.base.ColorRes
import com.core.wumfapp2020.base.StringRes
import wumf.com.detectphone.Country

class HomeTitle(private val strRes: StringRes, colorRes: ColorRes, type: Type = Type.IN_THE_WORLD,
                var country: Country? = null): ObservableField<Spannable>() {

    private val defaultCountry = Country(name = "Ukraine", code= "", mcc=0)

    var type: Type = type
        set(value) {
            field = value
            set(buildSpannable(strPrefix, getTagStr(strRes, value)))
        }

    private val strPrefix = strRes.getStr(R.string.the_most_useful_apps)
    private val textColor = colorRes.getColor(android.R.color.white)
    private val tagBgColor = colorRes.getColor(R.color.finn_alpha_69)

    init {
        set(buildSpannable(strPrefix, getTagStr(strRes, type)))
    }

    fun forciblyTextUpdate() {
        set(buildSpannable(strPrefix, getTagStr(strRes, type)))
    }

    private fun getTagStr(strRes: StringRes, type: Type) = when (type) {
        Type.IN_THE_WORLD -> strRes.getStrFromArray(R.array.type_of_apps, 0)
        Type.IN_ANOTHER_COUNTRY,
        Type.IN_MY_COUNTRY -> String.format(strRes.getStrFromArray(R.array.type_of_apps, 1), country?.name)
        Type.AMONG_FRIENDS -> strRes.getStrFromArray(R.array.type_of_apps, 3)
    }

    private fun buildSpannable(strPrefix: String, tag: String): Spannable {
        return TagSpannableBuilder(strPrefix, textColor, tagBgColor).appendTag(tag).build()
    }

    enum class Type {
        IN_THE_WORLD,
        IN_MY_COUNTRY,
        AMONG_FRIENDS,
        IN_ANOTHER_COUNTRY
    }

}