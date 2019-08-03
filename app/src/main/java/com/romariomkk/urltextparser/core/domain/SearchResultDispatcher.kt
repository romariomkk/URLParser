package com.romariomkk.urltextparser.core.domain

import com.romariomkk.urltextparser.core.pojo.SearchResult


interface SearchResultDispatcher {
    fun proceedResult(searchResult: SearchResult)
}