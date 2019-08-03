package com.romariomkk.urltextparser.core.domain

import android.util.Log
import com.romariomkk.urltextparser.core.pojo.QueryParams
import com.romariomkk.urltextparser.core.pojo.SearchResult
import com.romariomkk.urltextparser.util.Keys
import com.romariomkk.urltextparser.view.main.SearchNotifyContract
import java.util.*
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

class SearchManager(private val searchNotifyContract: SearchNotifyContract) {

    private lateinit var queryThreadPoolExecutor: QueryThreadPoolExecutor
    private val mLinksQueue = LinkedBlockingQueue<String>()
    private var mRunningTasks = ArrayList<Future<*>>()
    private var mProcessedLinksCounter = AtomicLong(1L)

    private var mLockObject = Any()
    private var mMaxRequests = 0

    init {
        queryThreadPoolExecutor = QueryThreadPoolExecutor(
            CORE_COUNT, CORE_COUNT,
            KEEP_ALIVE_TIME_VALUE, KEEP_ALIVE_TIME_UNIT,
            LinkedBlockingQueue(),
            QueryThreadFactory()
        )
    }

    fun startSearch(queryParams: QueryParams) {
        setupQueryThreadPoolExecutor(queryParams)
        val firstSearchResult =
            SearchResult(0L, queryParams.url, queryParams.searchText, executionResultStatus = Keys.FLAG_CREATED)
        searchNotifyContract.onFirstElementDone(firstSearchResult)

        val firstRunnable = QueryRunnable(firstSearchResult).apply {
            this.setSearchResultDispatcher(mSearchResultDispatcher)
        }
        addRunnable(firstRunnable)

    }

    private fun addRunnable(runnable: Runnable) {
        val future = queryThreadPoolExecutor.submit(runnable)
        mRunningTasks.add(future)
    }

    fun proceedResultAndNewRunnable(oldSearchResult: SearchResult) {
        if (oldSearchResult.executionResultStatus == Keys.FLAG_ERROR) {
            searchNotifyContract.onSearchResultAvailable(oldSearchResult)
            return
        }

        if (mLinksQueue.size < mProcessedLinksCounter.get()) {
            mLinksQueue.addAll(oldSearchResult.linkSet)
        }

        for (link in mLinksQueue) {
            if (mProcessedLinksCounter.get() < mMaxRequests) {
                val newSearchResult = SearchResult().apply {
                    id = mProcessedLinksCounter.get()
                    url = link
                    searchWord = oldSearchResult.searchWord
                    executionResultStatus = Keys.FLAG_CREATED
                }

                searchNotifyContract.onSearchResultAvailable(newSearchResult)

                Log.d(TAG, "Parsable link = $link")
                val newQueryRunnable = QueryRunnable(newSearchResult).apply {
                    setSearchResultDispatcher(mSearchResultDispatcher)
                }

                addRunnable(newQueryRunnable)
                mProcessedLinksCounter.getAndIncrement()

                //todo this code notifies live data too fast, it does not process al items properly
//                newSearchResult.executionResultStatus = Keys.FLAG_RUNNING
//                searchNotifyContract.onSearchResultAvailable(newSearchResult)
            } else {
                break
            }
        }

        searchNotifyContract.onSearchResultAvailable(oldSearchResult)
    }

    fun pauseResume() {
        if (queryThreadPoolExecutor.isPaused) {
            searchNotifyContract.onSearchResume()
        } else {
            searchNotifyContract.onSearchPause()
        }
        queryThreadPoolExecutor.switchPauseResume()
    }

    fun cancelAllTasks() {
        synchronized(mLockObject) {
            queryThreadPoolExecutor.clearTaskQueue()
            mLinksQueue.clear()
            resetCounter()

            for (task in mRunningTasks) {
                if (!task.isDone) {
                    task.cancel(true)
                }
            }
            mRunningTasks.clear()
        }

        searchNotifyContract.onStopSearch()
        resetLocks()
    }

    private val mSearchResultDispatcher = object : SearchResultDispatcher {
        override fun proceedResult(searchResult: SearchResult) {
            proceedResultAndNewRunnable(searchResult)
        }
    }

    /* BOILERPLATE */

    private fun resetCounter() {
        mProcessedLinksCounter.set(1L)
    }

    private fun resetLocks() {
        queryThreadPoolExecutor.forceResume()
    }

    private fun setupQueryThreadPoolExecutor(queryParams: QueryParams) {
        setCorePoolSize(queryParams.maxThreads)
        setMaximumPoolSize(queryParams.maxThreads)
        mMaxRequests = queryParams.maxURLs
    }

    private fun setCorePoolSize(maxThreads: Int) {
        queryThreadPoolExecutor.corePoolSize =
            if (maxThreads > CORE_COUNT) CORE_COUNT else maxThreads
    }

    private fun setMaximumPoolSize(maxThreads: Int) {
        queryThreadPoolExecutor.maximumPoolSize =
            if (maxThreads > CORE_COUNT) CORE_COUNT else maxThreads
    }

    companion object {
        private val TAG = SearchManager::class.java.simpleName

        private val CORE_COUNT = Runtime.getRuntime().availableProcessors()
        private const val KEEP_ALIVE_TIME_VALUE = 30L
        private val KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS
    }

}