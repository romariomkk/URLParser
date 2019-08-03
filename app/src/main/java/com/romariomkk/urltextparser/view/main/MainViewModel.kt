package com.romariomkk.urltextparser.view.main

import androidx.lifecycle.MutableLiveData
import com.romariomkk.urltextparser.core.domain.SearchManager
import com.romariomkk.urltextparser.core.pojo.QueryParams
import com.romariomkk.urltextparser.core.pojo.SearchResult
import com.romariomkk.urltextparser.util.Resource
import com.romariomkk.urltextparser.util.validator.QueryParamsValidator
import com.romariomkk.urltextparser.view.base.AbsViewModel

class MainViewModel : AbsViewModel() {

    private val mQueryValidator = QueryParamsValidator()
    private val mSearchManager by lazy {
        SearchManager(mSearchNotifyContract)
    }
    val queryParams = QueryParams()

    val validationLiveData: MutableLiveData<ValidityResult> = MutableLiveData()
    val searchResultSource = MutableLiveData<Resource<SearchResult>>()

    fun tryStartParsing() {
        val validity = mQueryValidator.checkValidity(queryParams)
        validationLiveData.value = validity
        if (validity == ValidityResult.ALL_OK) {
            startParsing(queryParams)
        }
    }

    private fun startParsing(queryParams: QueryParams) {
        searchResultSource.value = Resource.start()
        mSearchManager.startSearch(queryParams)
    }

    fun switchPauseResume() {
        mSearchManager.pauseResume()
    }

    fun stopSearch() {
        mSearchManager.cancelAllTasks()
    }

    private val mSearchNotifyContract = object: SearchNotifyContract {

        override fun onFirstElementDone(firstSearchResult: SearchResult) {
            searchResultSource.postValue(Resource.loading(firstSearchResult))
        }

        override fun onStopSearch() {
            searchResultSource.postValue(Resource.finish())
        }

        override fun onSearchResultAvailable(searchResult: SearchResult) {
            searchResultSource.postValue(Resource.success(searchResult))
        }

        override fun onSearchResume() {
            searchResultSource.postValue(Resource.resume())
        }

        override fun onSearchPause() {
            searchResultSource.postValue(Resource.pause())
        }

    }
}