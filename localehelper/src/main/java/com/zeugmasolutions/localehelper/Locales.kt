package com.zeugmasolutions.localehelper

import java.util.*

object Locales {

    /**
     * List of language codes for Right-To-Left languages
     */
    val RTL: Set<String> by lazy {
        hashSetOf(
            "ar",
            "dv",
            "fa",
            "ha",
            "he",
            "iw",
            "ji",
            "ps",
            "sd",
            "ug",
            "ur",
            "yi"
        )
    }
}