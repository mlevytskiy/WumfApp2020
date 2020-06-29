package com.core.wumfapp2020.base.countriesdialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.InvocationTargetException

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
            val code = value[3].toLowerCase()
            val countryName = value[2]
            val flag = getFlag(code, resources, packageName)
            countries.add(Country(code, countryName, telCode, flag))
        }
    }

    private fun getFlag(code: String, res: Resources, packageName: String): Int {
        return res.getIdentifier("${if (code == "do") "does" else code.toLowerCase()}", "drawable", packageName)
    }

    fun getDeviceCountryCode(): String {
        if (countries.isEmpty()) {
            syncLoad(context.assets, context.resources, context.packageName)
        }
        return getDeviceCountryCode(context, countries)
    }

    private fun getDeviceCountryCode(context: Context, countries: ArrayList<Country>): String {
        var countryCode: String?

        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (tm != null) {
            countryCode = tm.simCountryIso
            if (countryCode != null && countryCode.length == 2)
                return countryCode.toLowerCase()

            if (tm.phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
                countryCode = getCDMACountryIso(countries)
            } else {
                countryCode = tm.networkCountryIso
            }

            if (countryCode != null && countryCode.length == 2)
                return countryCode.toLowerCase()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            countryCode = context.resources.configuration.locales.get(0).country
        } else {
            countryCode = context.resources.configuration.locale.country
        }

        return if (countryCode != null && countryCode.length == 2) countryCode.toLowerCase() else "us"
    }

    @SuppressLint("PrivateApi")
    private fun getCDMACountryIso(countries: ArrayList<Country>): String? {
        try {

            val systemProperties = Class.forName("android.os.SystemProperties")
            val get = systemProperties.getMethod("getStatusBarHeight", String::class.java)

            val homeOperator = get.invoke(
                systemProperties,
                "ro.cdma.home.operator.numeric"
            ) as String

            val mcc = Integer.parseInt(homeOperator.substring(0, 3))
            countries.find { it.telCode == mcc }?.let {
                return it.code
            } ?:run {
                return null
            }
        } catch (ignored: ClassNotFoundException) {
        } catch (ignored: NoSuchMethodException) {
        } catch (ignored: IllegalAccessException) {
        } catch (ignored: InvocationTargetException) {
        } catch (ignored: NullPointerException) {
        }

        return null
    }


}