package com.onboarding.enterphonenumberui

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import wumf.com.detectphone.AppCountryDetector
import java.util.*

class LabeledEditText(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var editText: EditText? = null
    private var bindCustomKeyboard: Int? = null
    private var inAppKeyboard: PhoneNumberInAppKeyboard? = null
    private var hintText: String? = null
    private var lBackground: Int? = null
    private var borderColor: Int? = null
    private var mode: Int? = null
    private var text: String? = null

    var phoneNumberChangeListener: PhoneNumberChangeListener? = null

    private var phoneNumberUtil: PhoneNumberUtil? = null

    private var flag: ImageView? = null
    private var labelCountry: TextView? = null

    var detectedCountryMCC: Int = 0
        private set

    init {
        phoneNumberUtil = PhoneNumberUtil.createInstance(getContext().applicationContext)
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LabeledEditText,
            0, 0).apply {
            try {
                borderColor = getColor(R.styleable.LabeledEditText_borderColor, -1)
                lBackground = getColor(R.styleable.LabeledEditText_lBackground, -1)
                bindCustomKeyboard = getResourceId(R.styleable.LabeledEditText_bindCustomKeyboard, -1)
                hintText = getString(R.styleable.LabeledEditText_hintText)
                mode = getInt(R.styleable.LabeledEditText_mode, 1)
                text = getString(R.styleable.LabeledEditText_text)
            } finally {
                recycle()
            }
        }

        val view = View.inflate(getContext(), R.layout.view_phone_number_edit_text, this)
        val country = view.findViewById<View>(R.id.country)
        editText = view.findViewById(R.id.edit_text)
        country.setOnClickListener { Toast.makeText(context, "country", Toast.LENGTH_LONG).show() }

        labelCountry = view.findViewById(R.id.label_country)
        flag = view.findViewById(R.id.flag)

        if (mode == 0) {
            flag?.visibility = GONE
            labelCountry?.visibility = GONE
            hintText?.let{
                val label = view.findViewById<TextView>(R.id.label1)
                label.text = hintText
            }
            editText?.setBackgroundResource(R.drawable.labled_edit_text_background)
            editText?.setText(text)
        } else {
            editText?.isFocusable = false
            editText?.isClickable = false
        }

        editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                phoneNumberChangeListener?.onChange(s.toString())
            }
        })
        AppCountryDetector.fillMap(context)
    }

    fun enterPhoneByUser(phone: String): Int {
        val fixedPhone = prefillPhone(phone)
        try {
            val phoneNumber = phoneNumberUtil?.parseAndKeepRawInput(fixedPhone, "")
            editText?.setText(
                phoneNumberUtil?.format(
                    phoneNumber,
                    PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL
                )
            )
            try {
                setDetectedCountryCode(phoneNumber?.countryCode)
                detectedCountryMCC = phoneNumber?.countryCode ?: 0
                return detectedCountryMCC
            } catch (exc : java.lang.Exception) {
                //ignore
            }
        } catch (exception: Exception) {
            editText?.setText(fixedPhone)
        }
        detectedCountryMCC = 0
        return detectedCountryMCC
    }

    private fun prefillPhone(phone: String):String {
        if (phone.isBlank()) {
            return ""
        }
        if (phone.length > 3 && phone.startsWith("0")) {
            //use ukraine as default countryCode
            return "+38$phone"
        } else if (!phone.startsWith("+") && !phone.startsWith("0")) {
            return "+$phone"
        } else {
            return phone
        }
    }

    private fun setDetectedCountryCode(code: Int?) {
        val country = AppCountryDetector.detectCountryByPhoneCode(code)
        val codeIso = country?.code?.toLowerCase(Locale.ROOT)
        if (!codeIso.isNullOrBlank()) {
            try {
                val id = resources.getIdentifier("ic_$codeIso", "drawable", resources.getResourcePackageName(R.drawable.ic_ad))
                flag?.setImageDrawable(resources.getDrawable(id))
            } catch (resourceNotFound: Resources.NotFoundException) {
                //ignore
            }
            val name = country.name
            labelCountry?.text = name
        }
    }

    fun getPhoneNumberStr() = editText?.text.toString()

    fun setBindCustomKeyboard(value: Int, parentView: View) {
        bindCustomKeyboard = value
        bindKeyboard(parentView)
    }

    fun unbindCustomKeyboard() {
        inAppKeyboard?.setListener(null)
        inAppKeyboard = null
        bindCustomKeyboard = null
    }

    private fun bindKeyboard(parentView: View) {
        bindCustomKeyboard?.let {
            inAppKeyboard = parentView.findViewById(it)

            inAppKeyboard?.showPlusButton()
            inAppKeyboard?.setListener(object: OnKeyboardClickListener {
                override fun enterLetter(letter: String) {
                    enterPhoneByUser(editText?.text.toString() + letter)
                }

                override fun deleteLastLetter() {
                    if (editText!!.text.length > 0) {
                        enterPhoneByUser(editText!!.text!!.substring(0, editText!!.text.length - 1))
                    } else {
                        editText?.setText("")
                    }
                }
            })
        }
    }

    interface PhoneNumberChangeListener {
        fun onChange(str: String)
    }
}