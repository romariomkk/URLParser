package com.romariomkk.urltextparser.core.domain

import android.util.Log
import java.util.concurrent.ThreadFactory

class QueryThreadFactory: ThreadFactory {

    companion object {
        private val TAG = QueryThreadFactory::class.java.simpleName
    }

    override fun newThread(r: Runnable?): Thread {
        return Thread(r).apply {
            name = "LeizerQueryThread"
            priority = Thread.NORM_PRIORITY
            uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { t, e ->
                Log.e(TAG, name + " error: " + e.message)
            }
        }
    }
}