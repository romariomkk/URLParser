package com.romariomkk.urltextparser

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho

class ParserApp: Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    companion object {
        private var INSTANCE: ParserApp? = null

        @JvmStatic
        fun get(): ParserApp = INSTANCE!!
    }
}