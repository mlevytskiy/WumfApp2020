package com.core.wumfapp2020.util

import android.content.Context
import android.content.res.Configuration
import com.core.wumfapp2020.R
import com.core.wumfapp2020.memory.LanguagePreferencesDelegate
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CurrentLanguageModel @Inject constructor(private val context: Context) {

    private val languagesList: MutableList<Pair<String, String>> = ArrayList()
    private val defaultLanguage: Pair<String, String>
    private var language by LanguagePreferencesDelegate()

    init {
        val languagesArray = context.resources.getStringArray(R.array.languages)
        defaultLanguage = Pair("en", languagesArray[0])
        languagesList.add(Pair("ja", languagesArray[1]))
        languagesList.add(Pair("de", languagesArray[2]))
        languagesList.add(Pair("fr", languagesArray[3]))
        languagesList.add(Pair("ru", languagesArray[4]))
        languagesList.add(Pair("es", languagesArray[5]))
        languagesList.add(defaultLanguage)

    }

    fun detectCurrentLanguage(): String {
        val language = Locale.getDefault().language
        languagesList.find { it.first.equals(language, true) }?.let {
            return it.second
        }
        return defaultLanguage.second
    }

    fun setCurrentLanguage(language: String) {
        val languageCode: String? = languagesList.find { it.second.equals(language, true) }?.first
        languageCode?.let {
            setLocale(it)
        }
    }

    fun getFullLanguageName(shortName: String): String? {
        return languagesList.find { it.first.equals(shortName, true) }?.second
    }

    fun getShortLanguageName(fullName: String) : String? {
        return languagesList.find { it.second.equals(fullName, true) }?.first
    }

    fun setLocale(localeStr: String?) {
        localeStr?.let {
            val locale = Locale(localeStr)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            context.resources.updateConfiguration(config, null)
            language = localeStr
        }
    }
}