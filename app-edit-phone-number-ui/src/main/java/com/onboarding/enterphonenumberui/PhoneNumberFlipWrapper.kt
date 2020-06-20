package com.onboarding.enterphonenumberui

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.onboarding.enterphonenumberui.CodeEditText.CodeChangeListener
import com.onboarding.enterphonenumberui.LabeledEditText.PhoneNumberChangeListener

class PhoneNumberFlipWrapper(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val rightOut: AnimatorSet
    private val leftIn: AnimatorSet
    private val cardBackLayout: View
    private val cardFrontLayout: View
    private var isBackVisible = false
    private var bindCustomKeyboard: Int? = null
    private var phoneNumberEditText: LabeledEditText? = null
    private var codeEditText: CodeEditText? = null
    var phoneNumberChangeListener: PhoneNumberChangeListener? = null
        set(value) {
            field = value
            phoneNumberEditText?.phoneNumberChangeListener = value
        }
    var codeChangeListener: CodeChangeListener? = null
        set(value) {
            field = value
            codeEditText?.codeChangeListener = value
        }

    init {
        val view = View.inflate(getContext(), R.layout.view_phone_number_flip_wrapper, this)
        rightOut = AnimatorInflater.loadAnimator(context, R.animator.out_animation) as AnimatorSet
        leftIn = AnimatorInflater.loadAnimator(context, R.animator.in_animation) as AnimatorSet
        cardFrontLayout = view.findViewById(R.id.card_front)
        cardBackLayout = view.findViewById(R.id.card_back)
        phoneNumberEditText = cardFrontLayout.findViewById(R.id.phone_number_edit_text)
        codeEditText = cardBackLayout.findViewById(R.id.code_edit_text)
        changeCameraDistance()
    }

    init { //init view attributes
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PhoneNumberFlipWrapper,
            0, 0).apply {
            try {
                bindCustomKeyboard = getResourceId(R.styleable.PhoneNumberFlipWrapper_customKeyboard, -1)
            } finally {
                recycle()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        bindKeyboard()
    }

    private fun bindKeyboard() {
        bindCustomKeyboard?.let {
            if (!isBackVisible) {
                phoneNumberEditText?.setBindCustomKeyboard(it, parent as View)
            } else {
                codeEditText?.setBindCustomKeyboard(it, parent as View)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        phoneNumberEditText?.unbindCustomKeyboard()
        codeEditText?.unbindCustomKeyboard()
    }

    private fun changeCameraDistance() {
        val distance = 3000
        val scale = resources.displayMetrics.density * distance
        cardFrontLayout.setCameraDistance(scale)
        cardBackLayout.setCameraDistance(scale)
    }

    fun getPhoneNumberStr() = phoneNumberEditText?.getPhoneNumberStr()

    fun enterPhoneByUser(value: String) {
        phoneNumberEditText?.enterPhoneByUser(value)
    }

//    fun flipCard() {
//        if (!isBackVisible) {
//            fillBackCard()
//            rightOut.setTarget(cardFrontLayout)
//            leftIn.setTarget(cardBackLayout)
//            rightOut.start()
//            leftIn.start()
//            isBackVisible = true
//        } else {
//            rightOut.setTarget(cardBackLayout)
//            leftIn.setTarget(cardFrontLayout)
//            rightOut.start()
//            leftIn.start()
//            isBackVisible = false
//        }
//        bindKeyboard()
//    }

    fun showBackCard() {
        if (!isBackVisible) {
            fillBackCard()
            rightOut.setTarget(cardFrontLayout)
            leftIn.setTarget(cardBackLayout)
            rightOut.start()
            leftIn.start()
            isBackVisible = true
            bindKeyboard()
        }
    }

    fun getCode() = codeEditText?.getCode()

    fun isBackCardShowed() = isBackVisible

    private fun fillBackCard() {
        codeEditText?.setPhoneNumber(phoneNumberEditText?.getPhoneNumberStr())
    }

}