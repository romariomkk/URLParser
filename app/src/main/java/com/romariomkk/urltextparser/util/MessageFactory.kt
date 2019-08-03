package com.romariomkk.urltextparser.util

import android.os.Message
import androidx.annotation.IntDef
import com.romariomkk.urltextparser.core.pojo.SearchResult

class MessageFactory {

    companion object {
        const val MSG_ID_SUCCESS = 6199
        const val MSG_ID_PAUSE = 69
        const val MSG_ID_RESUME = 96
        const val MSG_ID_ABORT = 699

        @IntDef(MSG_ID_SUCCESS, MSG_ID_PAUSE, MSG_ID_RESUME, MSG_ID_ABORT)
        annotation class MessageId

        @JvmStatic
        fun searchResultMessage(searchResult: SearchResult): Message {
            return Message().apply {
                what = MSG_ID_SUCCESS
                obj = searchResult
            }
        }

        @JvmStatic
        fun pauseMessage(msg: String) = createTextMessage(MSG_ID_PAUSE, msg)

        @JvmStatic
        fun resumeMessage(msg: String) = createTextMessage(MSG_ID_RESUME, msg)

        @JvmStatic
        fun abortMessage(msg: String) = createTextMessage(MSG_ID_ABORT, msg)

        @JvmStatic
        private fun createTextMessage(@MessageId id: Int, msg: String): Message {
            return Message().apply {
                what = id
                obj = msg
            }
        }
    }
}

fun Message.getSearchResult(): SearchResult {
    return this.obj as SearchResult
}