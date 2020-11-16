package com.core.wumfapp2020

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.core.wumfapp2020.di.AppComponent
import com.core.wumfapp2020.di.DaggerAppComponent
import com.core.wumfapp2020.di.DaggerComponentProvider
import com.google.android.play.core.splitcompat.SplitCompat
import com.library.core.unsyncLazy
import com.zeugmasolutions.localehelper.LocaleHelperApplicationDelegate

class DynamicApp: Application(), DaggerComponentProvider {

    private val localeAppDelegate = LocaleHelperApplicationDelegate()

    companion object {
        @JvmStatic
        var instance: DynamicApp? = null
    }

    override val component: AppComponent by unsyncLazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun attachBaseContext(base: Context) {
        val newContext = localeAppDelegate.attachBaseContext(base)
        super.attachBaseContext(newContext)
        SplitCompat.install(newContext)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeAppDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context? {
        return localeAppDelegate.getApplicationContext(this)
    }

//    /**
//     * A singleton helper for storing and retrieving the user selected language in a
//     * SharedPreferences instance. It is required for persisting the user language choice between
//     * application restarts.
//     */
//    object LanguageHelper {
//        val language = "ru"
//
//        fun init(ctx: Context) {
//        }
//
//        /**
//         * Get a Context that overrides the language selection in the Configuration instance used by
//         * getResources() and getAssets() by one that is stored in the LanguageHelper preferences.
//         *
//         * @param ctx a base context to base the new context on
//         */
//        fun getLanguageConfigurationContext(ctx: Context): Context {
//            val conf = getLanguageConfiguration()
//            return ctx.createConfigurationContext(conf)
//        }
//
//        /**
//         * Get an empty Configuration instance that only sets the language that is
//         * stored in the LanguageHelper preferences.
//         * For use with Context#createConfigurationContext or Activity#applyOverrideConfiguration().
//         */
//        fun getLanguageConfiguration(): Configuration {
//            val conf = Configuration()
//            conf.setLocale(Locale.forLanguageTag(language))
//            return conf
//        }
//    }
}