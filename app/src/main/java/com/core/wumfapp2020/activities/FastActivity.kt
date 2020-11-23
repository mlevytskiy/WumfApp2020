package com.core.wumfapp2020.activities

import android.animation.ArgbEvaluator
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import com.core.wumfapp2020.DynamicApp
import com.core.wumfapp2020.R
import com.core.wumfapp2020.WaveInterpolator
import com.core.wumfapp2020.base.showCountriesDialog
import com.core.wumfapp2020.base.showLanguagesDialog
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.memory.LanguagePreferencesDelegate
import com.core.wumfapp2020.util.CountriesUtil
import com.core.wumfapp2020.util.CurrentLanguageModel
import com.library.core.log
import com.onboarding.enterphonenumberui.TagEditText
import com.zeugmasolutions.localehelper.LocaleHelper
import wumf.com.detectphone.AppCountryDetector
import wumf.com.detectphone.Country
import java.util.*


class FastActivity : ChangeLanguageActivity() {

    private val memory by lazy {
        injector.provideUserInfoRepository()
    }

    private var language by LanguagePreferencesDelegate()
    private val currLanguage = CurrentLanguageModel(DynamicApp.instance!!)
    private var currCountry: Country? = null
    private var languageView: TagEditText? = null
    private var countryView: TagEditText? = null

    private val languagesList: MutableList<Pair<String, String>> = ArrayList()


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        window.apply {
            val currentStatusBarColor = Color.TRANSPARENT
            val currentNavBarColor = Color.TRANSPARENT
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = currentStatusBarColor
            navigationBarColor = currentNavBarColor
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
        super.onCreate(savedInstanceState)
        if (!memory.isEmpty()) {
            nextScreen()
            return
        }
        setContentView(R.layout.activity_fast)

        languageView = findViewById(R.id.language)
        val local = LocaleHelper.getLocale(this)
        languageView?.setTagLabel(getString(R.string.language))
        val language = local.displayLanguage.capitalize()
        languageView?.setTagValue(language)

        countryView = findViewById(R.id.country)

        currCountry = getCountry(this)
        val countryName = currCountry?.name ?: local.displayCountry.capitalize()

        countryView?.setTagLabel(getString(R.string.country))
        countryView?.setTagValue(countryName)
        val bgView = findViewById<View>(R.id.bg_view)
        "FastActivity onCreate()".log()
        val root = findViewById<View>(R.id.root)
        findViewById<MotionLayout>(R.id.root).setTransitionListener(object : TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
//                "p1=$p1 p2=$p2 p3=$p3".log()
                setWindowsColor(p3)
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {

            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }

        })
        val rotate = AnimationUtils.loadAnimation(this, R.anim.splash_screen_rotate)
        rotate.interpolator = WaveInterpolator()

        rotate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                val welcomeText = this@FastActivity.resources.getString(R.string.welcome_text)
                findViewById<TextView>(R.id.message).text = Html.fromHtml(welcomeText)
                val rootView = findViewById<MotionLayout>(R.id.root)
                rootView.transitionToEnd()
            }

            override fun onAnimationStart(animation: Animation?) {}

        })
        val image = findViewById<View>(R.id.image)
        image.startAnimation(rotate)

        val languagesArray = resources.getStringArray(R.array.languages)
        languagesList.add(Pair("ja", languagesArray[1]))
        languagesList.add(Pair("de", languagesArray[2]))
        languagesList.add(Pair("fr", languagesArray[3]))
        languagesList.add(Pair("ru", languagesArray[4]))
        languagesList.add(Pair("es", languagesArray[5]))
        languagesList.add(Pair("en", languagesArray[0]))
    }

    fun onClickGo(view: View) {
        nextScreen()
    }

    fun nextScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    val evaluator = ArgbEvaluator()
    val startStatusBarColor = Color.TRANSPARENT
    val endStatusBarColor = Color.WHITE
    val startNavColor = Color.TRANSPARENT
    val endNavColor by lazy { resources.getColor(R.color.gray4) }

    fun setWindowsColor(changeIndicator: Float) {
        window.apply {
            val currentStatusBarColor = evaluator.evaluate(changeIndicator, startStatusBarColor, endStatusBarColor) as Int
            val currentNavBarColor = evaluator.evaluate(changeIndicator, startNavColor, endNavColor) as Int
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = currentStatusBarColor
            navigationBarColor = currentNavBarColor
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }


    fun onClickLanguage(view: View) {
        showLanguagesDialog(this, { fullName ->
            val language = languagesList.find { it.second.equals(fullName, true) }?.first
            language?.let {
                localeDelegate.setLocaleWithoutRestartScreen(this, Locale.forLanguageTag(it), {
                    resources.updateConfiguration(it, resources.displayMetrics)
                    super.onConfigurationChanged(it)
                    val welcomeText = this@FastActivity.resources.getString(R.string.welcome_text)
                    findViewById<TextView>(R.id.message).text = Html.fromHtml(welcomeText)
                    languageView?.setTagLabel(getString(R.string.language))
                    languageView?.setTagValue(fullName)
                    countryView?.setTagLabel(getString(R.string.country))
                    currCountry?.let {
                        val mcc = it.mcc
                        val country = CountriesUtil.syncLoad(view.context).find {
                            it.mcc == mcc
                        }
                        country?.let {
                            countryView?.setTagValue(it.name)
                        }
                    }
                })
            }
        })
        //startActivity(Intent(this, PickLanguageActivity::class.java))
    }

    var countryDialog: DialogInterface? = null
    fun onClickCountry(view: View) {
        countryDialog = showCountriesDialog(this, {
            currCountry = it
            AppCountryDetector.lastPickedCountry = it
            val countryView: TagEditText = findViewById(R.id.country)
            countryView.setTagValue(it.name)
            countryDialog?.dismiss()
            countryDialog = null
        })
        //startActivity(Intent(this, PickCountryActivity::class.java))
    }

    private fun getCountry(context: Context) : Country? {
        val iso = getUserCountryIso(context)
        if (iso == null) {
            return null
        }
        return CountriesUtil.syncLoad(context).find { country-> country.code == iso }
    }

    private fun getUserCountryIso(context: Context): String? {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simCountry = tm.simCountryIso
            if (simCountry != null && simCountry.length == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US)
            } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                val networkCountry = tm.networkCountryIso
                if (networkCountry != null && networkCountry.length == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US)
                }
            }
        } catch (e: Exception) {
        }
        return null
    }

}