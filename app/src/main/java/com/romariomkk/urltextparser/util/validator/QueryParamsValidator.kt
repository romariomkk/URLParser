package com.romariomkk.urltextparser.util.validator

import android.webkit.URLUtil
import com.romariomkk.urltextparser.core.pojo.QueryParams
import com.romariomkk.urltextparser.view.main.ValidityResult

class QueryParamsValidator: ValidityChecker<QueryParams, ValidityResult> {
    override fun checkValidity(item: QueryParams): ValidityResult {
        return when {
            !URLUtil.isNetworkUrl(item.url) -> {
                ValidityResult.ERROR_URL
            }
            item.maxThreads < 1 -> {
                ValidityResult.ERROR_MAX_THREADS
            }
            item.searchText.isEmpty() -> {
                ValidityResult.ERROR_SEARCH_WORD
            }
            item.maxURLs < 1 -> {
                ValidityResult.ERROR_RESULT_LIMIT
            }
            else -> ValidityResult.ALL_OK
        }
    }
}