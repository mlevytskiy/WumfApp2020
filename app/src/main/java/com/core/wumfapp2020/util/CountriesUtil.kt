package com.core.wumfapp2020.util

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.text.TextUtils
import wumf.com.detectphone.Country
import wumf.com.detectphone.R
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

object CountriesUtil {

    fun syncLoad(context: Context): ArrayList<Country> {
        return syncLoad(context.resources)
    }

    private fun syncLoad(resources: Resources): ArrayList<Country> {
        val countries = ArrayList<Country>()
        val inputStream: InputStream = resources.openRawResource(R.raw.countries)
        val inputreader = InputStreamReader(inputStream)
        val reader = BufferedReader(inputreader)
        for (item in reader.lineSequence()) {
            val value = TextUtils.split(item, ",")
            val telCode = Integer.parseInt(value[6])
            val code = value[3].toLowerCase(Locale.ROOT)
            val countryName = value[2]
            countries.add(Country(code=code, name = countryName.replace("\"", ""), mcc=telCode))
        }
        return countries
    }


}