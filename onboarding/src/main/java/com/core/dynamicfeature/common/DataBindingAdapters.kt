package com.core.dynamicfeature.common

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.viewpager.widget.ViewPager
import com.onboarding.enterphonenumberui.CodeEditText
import com.onboarding.enterphonenumberui.CodeEditText.CodeChangeListener
import com.onboarding.enterphonenumberui.LabeledEditText
import com.onboarding.enterphonenumberui.PhoneNumberFlipWrapper
import java.io.File

object DataBindingAdapters {

    @BindingAdapter("code")
    @JvmStatic fun setCode(view: PhoneNumberFlipWrapper, code: String) {
        view.setCode(code)
    }

    @InverseBindingAdapter(attribute = "code")
    @JvmStatic fun getCode(view: PhoneNumberFlipWrapper) : String {
        return view.getCode()
    }

    @JvmStatic
    @BindingAdapter("codeAttrChanged")
    fun setCodeChangeListener(view: PhoneNumberFlipWrapper, bindingListener: InverseBindingListener) {
        view.codeChangeListener = object: CodeChangeListener {
            override fun onChange(str: String) {
                bindingListener.onChange()
            }
        }
    }

    @BindingAdapter("phoneNumber")
    @JvmStatic fun setPhoneNumber(view: PhoneNumberFlipWrapper, phoneNumber: String) {
        view.phoneNumber = phoneNumber
    }

    @InverseBindingAdapter(attribute = "phoneNumber")
    @JvmStatic fun getPhoneNumber(view: PhoneNumberFlipWrapper) : String {
        return view.getPhoneNumberStr() ?: ""
    }

    @JvmStatic
    @BindingAdapter("phoneNumberAttrChanged")
    fun setPhoneNumberChangeListener(view: PhoneNumberFlipWrapper, bindingListener: InverseBindingListener) {
        view.phoneNumberChangeListener = object: LabeledEditText.PhoneNumberChangeListener {
            override fun onChange(str: String) {
                bindingListener.onChange()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun bindVisibility(view: View, visibility: Boolean) {
        view.visibility = if (visibility) View.VISIBLE else View.GONE
    }

}