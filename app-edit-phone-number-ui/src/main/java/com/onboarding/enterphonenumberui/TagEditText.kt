package com.onboarding.enterphonenumberui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

class TagEditText(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var editText: TextView? = null
    private var label: TextView? = null

    init {
        val view = View.inflate(getContext(), R.layout.view_language_code_edit_text, this)
        editText = view.findViewById(R.id.edit_text)
        label = view.findViewById(R.id.label1)
    }

    fun setTagValue(str: String) {
        editText?.text = str
    }

    fun setTagLabel(str: String) {
        label?.text = str
    }

}