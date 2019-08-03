package com.romariomkk.urltextparser.view.main

import com.romariomkk.urltextparser.core.pojo.SearchResult

interface SearchNotifyContract {

    fun onFirstElementDone(firstSearchResult: SearchResult)

    fun onSearchResultAvailable(searchResult: SearchResult)

    fun onStopSearch()

    fun onSearchResume()

    fun onSearchPause()
}