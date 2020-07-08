package wumf.com.detectphone

import android.content.Context
import android.text.TextUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.HashMap


object AppCountryDetector {

    private val countryCodes: HashMap<Int, String> = HashMap()
    private val countryNames: HashMap<Int, String> = HashMap()

    fun detectCountryByPhoneCode(mcc: Int?): Country? {
        val codeIso = countryCodes[mcc]?.toLowerCase(Locale.ROOT) ?: ""
        val name = countryNames[mcc] ?: ""
        mcc?.let {
            return Country(name=name, code = codeIso, mcc = mcc)
        }
        return null
    }

    fun isMapsEmpty(): Boolean {
        return countryCodes.isEmpty()
    }

    fun fillMap(context: Context) {
        val reader = BufferedReader(InputStreamReader(context.assets.open("countries.csv")))
        for (item in reader.lineSequence()) {
            val value = TextUtils.split(item, ",")
            val code = Integer.parseInt(value[6])
            countryCodes[code] = value[3]
            countryNames[code] = value[2]
        }
    }


}