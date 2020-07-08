package com.core.wumfapp2020

import android.app.Application
import android.content.Context
import com.core.wumfapp2020.di.AppComponent
import com.core.wumfapp2020.di.DaggerAppComponent
import com.core.wumfapp2020.di.DaggerComponentProvider
import com.google.android.play.core.splitcompat.SplitCompat
import com.library.core.unsyncLazy

class DynamicApp: Application(), DaggerComponentProvider {
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

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}