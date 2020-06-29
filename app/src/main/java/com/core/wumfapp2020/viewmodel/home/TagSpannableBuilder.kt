package com.core.wumfapp2020.viewmodel.home

import android.text.SpannableStringBuilder
import android.text.Spanned

class TagSpannableBuilder(str: String, private val textColor: Int, private val tagColor: Int) {

    private var stringBuilder: SpannableStringBuilder = SpannableStringBuilder(str)

    fun appendTag(tag: String): TagSpannableBuilder {
        val fullTag = "  " + tag + "  "
        stringBuilder.append(fullTag)
        stringBuilder.setSpan(
            TagBadgeSpannable(textColor, tagColor),
            stringBuilder.length - fullTag.length,
            stringBuilder.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return this
    }

    fun build() = stringBuilder
}