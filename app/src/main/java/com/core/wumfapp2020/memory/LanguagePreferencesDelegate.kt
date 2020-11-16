package com.core.wumfapp2020.memory

import android.util.SparseArray
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private const val LANGUAGE_KEY = "language"

class LanguagePreferencesDelegate : ReadWriteProperty<Any?, String> {

    var languageCode by PreferencesDelegate(LANGUAGE_KEY, -1)
    private val languages = SparseArray<String>().apply {
        put(0, "en")
        put(1, "ja")
        put(2, "de")
        put(3, "fr")
        put(4, "ru")
        put(5, "es")
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return languages.get(languageCode, "")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        val code = languages.indexOfValue(value)
        if (code == -1) {
            return
        }
        languageCode = code
    }

}