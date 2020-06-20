package com.onboarding.enterphonenumberui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class PhoneNumberInAppKeyboard(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var listener: OnKeyboardClickListener? = null
    private var plusBtn: View? = null

    init {
        val view = View.inflate(getContext(), R.layout.view_phone_number_in_app_keyboard, this)
        view.findViewById<View>(R.id.button_0).setOnClickListener { listener?.enterLetter("0") }
        view.findViewById<View>(R.id.button_1).setOnClickListener { listener?.enterLetter("1") }
        view.findViewById<View>(R.id.button_2).setOnClickListener { listener?.enterLetter("2") }
        view.findViewById<View>(R.id.button_3).setOnClickListener { listener?.enterLetter("3") }
        view.findViewById<View>(R.id.button_4).setOnClickListener { listener?.enterLetter("4") }
        view.findViewById<View>(R.id.button_5).setOnClickListener { listener?.enterLetter("5") }
        view.findViewById<View>(R.id.button_6).setOnClickListener { listener?.enterLetter("6") }
        view.findViewById<View>(R.id.button_7).setOnClickListener { listener?.enterLetter("7") }
        view.findViewById<View>(R.id.button_8).setOnClickListener { listener?.enterLetter("8") }
        view.findViewById<View>(R.id.button_9).setOnClickListener { listener?.enterLetter("9") }
        plusBtn = view.findViewById<View>(R.id.button_plus)
        plusBtn?.setOnClickListener { listener?.enterLetter("+")}
        view.findViewById<View>(R.id.button_delete).setOnClickListener { listener?.deleteLastLetter() }
    }

    fun setListener(value: OnKeyboardClickListener?) {
        listener = value
    }

    fun showPlusButton() {
        plusBtn?.isEnabled = true
    }

    fun hidePlusButton() {
        plusBtn?.isEnabled = false
    }

}