package com.romariomkk.urltextparser.view.base

import android.graphics.Color
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.romariomkk.urltextparser.R
import com.romariomkk.urltextparser.core.pojo.QueryError
import com.romariomkk.urltextparser.core.pojo.SearchResult
import com.romariomkk.urltextparser.util.Keys
import java.util.*

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("matches")
    fun bindSearchMatches(textView: TextView, searchResult: SearchResult) {
        textView.text = String.format(
            Locale.getDefault(),
            textView.context.getString(R.string.search_match), searchResult.searchWord, searchResult.matches
        )
    }

    @JvmStatic
    @BindingAdapter("android:background")
    fun bindCardViewBackground(frameLayout: FrameLayout, searchResult: SearchResult) {
        frameLayout.setBackgroundResource(
            Keys.SEARCH_RESULT_BGS[searchResult.executionResultStatus]!!
        )
    }

    @JvmStatic
    @BindingAdapter("error")
    fun bindErrorConfigs(textView: TextView, searchResult: SearchResult) {
        textView.run {
            when {
                searchResult.error.code == QueryError.EMPTY -> {
                    text = context.getString(R.string.no_info)
                    setTextColor(Color.BLACK)
                }
                searchResult.error.code == QueryError.NO_ERROR -> {
                    text = context.getString(R.string.no_errors)
                    setTextColor(Color.GREEN)
                }
                searchResult.error.message.isNotEmpty() -> {
                    text = String.format(Locale.getDefault(), context.getString(R.string.error_format), searchResult.error.code, searchResult.error.message)
                    setTextColor(Color.RED)
                }
            }
        }
    }
}