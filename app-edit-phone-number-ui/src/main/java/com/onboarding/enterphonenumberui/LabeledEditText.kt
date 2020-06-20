package com.onboarding.enterphonenumberui

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.graphics.Shader
import android.graphics.LinearGradient
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import java.io.BufferedReader
import java.io.InputStreamReader


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

    private val countryCodes: HashMap<Int, String> = HashMap()
    private val countryNames: HashMap<Int, String> = HashMap()

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

        fillMap()

    }

    fun enterPhoneByUser(phone: String) {
        try {
            val phoneNumber = phoneNumberUtil?.parseAndKeepRawInput(phone, "")
            editText?.setText(
                phoneNumberUtil?.format(
                    phoneNumber,
                    PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL
                )
            )
            try {
                setDetectedCountryCode(phoneNumber?.countryCode)
            } catch (exc : java.lang.Exception) {
                //ignore
            }
        } catch (exception: Exception) {
            editText?.setText(phone)
        }
    }

    private fun setDetectedCountryCode(code: Int?) {
        val codeIso = countryCodes[code]?.toLowerCase()
        if (codeIso != "") {
            try {
                val id = resources.getIdentifier("ic_" + codeIso, "drawable", context.packageName)
                flag?.setImageDrawable(resources.getDrawable(id))
            } catch (resourceNotFound: Resources.NotFoundException) {
                //ignore
            }
            labelCountry?.setText(countryNames[code])
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

    private fun fillMap() {
        val reader = BufferedReader(InputStreamReader(context.assets.open("countries.csv")))
        for (item in reader.lineSequence()) {
            val value = TextUtils.split(item, ",")
            val code = Integer.parseInt(value[6])
            countryCodes.put(code, value[3])
            countryNames.put(code, value[2])
        }
    }

    interface PhoneNumberChangeListener {
        fun onChange(str: String)
    }
}