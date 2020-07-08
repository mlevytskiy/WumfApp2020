package com.core.wumfapp2020.base.countriesdialog

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.text.TextUtils
import com.core.wumfapp2020.DynamicApp
import wumf.com.detectphone.Country
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList

fun Country.getFlagId(): Int? {
    val res = DynamicApp.instance?.resources!!
    val packageName = DynamicApp.instance?.packageName!!
    return res.getIdentifier(if (code == "do") "does" else code.toLowerCase(Locale.ROOT), "drawable", packageName)
}

class CountriesHolder(private val context: Context) {

    val countries = ArrayList<Country>()

    fun syncLoad() {
        syncLoad(context.assets, context.resources, context.packageName)
    }

    private fun syncLoad(assetManager: AssetManager, resources: Resources, packageName: String) {
        countries.clear()
        val reader = BufferedReader(InputStreamReader(assetManager.open("countries.csv")))
        for (item in reader.lineSequence()) {
            val value = TextUtils.split(item, ",")
            val telCode = Integer.parseInt(value[6])
            val code = value[3].toLowerCase(Locale.ROOT)
            val countryName = value[2]
            countries.add(Country(code=code, name = countryName, mcc=telCode))
        }
    }

}