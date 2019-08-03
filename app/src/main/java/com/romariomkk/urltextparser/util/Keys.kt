package com.romariomkk.urltextparser.util

import androidx.annotation.IntDef

class Keys {

    companion object {
        const val ERROR_MIME_SSL = 1996

        const val FLAG_CREATED = 1
        const val FLAG_RUNNING = 2
        const val FLAG_FINISHED = 3
        const val FLAG_ERROR = 4

        @IntDef(FLAG_CREATED, FLAG_RUNNING, FLAG_FINISHED, FLAG_ERROR)
        annotation class SearchExecutionFlag

        @JvmStatic
        val SEARCH_RESULT_BGS = hashMapOf(
            FLAG_CREATED to android.R.color.holo_blue_dark,
            FLAG_RUNNING to android.R.color.holo_orange_dark,
            FLAG_ERROR to android.R.color.holo_red_dark,
            FLAG_FINISHED to android.R.color.holo_green_dark
            )
    }
}