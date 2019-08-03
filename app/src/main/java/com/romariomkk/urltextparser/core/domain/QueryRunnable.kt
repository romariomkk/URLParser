
package com.romariomkk.urltextparser.core.domain

import android.util.Log
import android.webkit.URLUtil
import com.romariomkk.urltextparser.core.pojo.QueryError
import com.romariomkk.urltextparser.core.pojo.SearchResult
import com.romariomkk.urltextparser.util.Keys
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.UnsupportedMimeTypeException
import org.jsoup.select.Elements
import java.lang.ref.WeakReference
import javax.net.ssl.SSLException

class QueryRunnable(private val searchResult: SearchResult): Runnable {

    private var searchResultDispatcherRef: WeakReference<SearchResultDispatcher>? = null

    override fun run() {
        val regex = StringBuilder()
            .append("\\b").append(searchResult.searchWord).append("\\b").toString()

        try {
            if (Thread.interrupted()) {
                Thread.currentThread().interrupt()
                //throw InterruptedException()
            }

            val htmlDoc = Jsoup.connect(searchResult.url).get()
            val allRefs = htmlDoc.select("a[href]")
            val wordMatches = htmlDoc.getElementsMatchingOwnText(regex)

            searchResult.run {
                matches = wordMatches.size
                linkSet = allRefs.toLinkSet()
                error.code = QueryError.NO_ERROR
                executionResultStatus = Keys.FLAG_FINISHED
            }

            sendResult(searchResult)

        } catch (exc: Exception) {
            Log.e(TAG, searchResult.url)
            exc.printStackTrace()

            processException(exc)
        }
    }

    private fun Elements.toLinkSet(): HashSet<String> {
        val linkSet = HashSet<String>()
        for (singleLink in this) {
            val href = singleLink.attr("href")
            if (URLUtil.isNetworkUrl(href) && !href.matches("^.*\\.pdf$".toRegex())) {
                linkSet += href
            }
        }
        return linkSet
    }

    private fun processException(exc: Exception) {
        when (exc) {
            is HttpStatusException -> {
                searchResult.error.code = exc.statusCode
            }
            is UnsupportedMimeTypeException, is SSLException -> {
                searchResult.error.code = Keys.ERROR_MIME_SSL
            }
            else -> {
                searchResult.error.code = 500
            }
        }
        searchResult.run {
            error.message = exc.message!!
            executionResultStatus = Keys.FLAG_ERROR
        }

        sendResult(searchResult)
    }

    private fun sendResult(searchResult: SearchResult) {
        searchResultDispatcherRef?.get()?.proceedResult(searchResult)
    }

    public fun setSearchResultDispatcher(searchResultDispatcher: SearchResultDispatcher) {
        searchResultDispatcherRef = WeakReference(searchResultDispatcher)
    }

    companion object {
        private val TAG = QueryRunnable::class.java.simpleName
    }

}