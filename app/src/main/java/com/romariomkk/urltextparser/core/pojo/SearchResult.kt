package com.romariomkk.urltextparser.core.pojo

import android.os.Parcelable
import androidx.annotation.IntDef
import com.romariomkk.urltextparser.util.Keys
import kotlinx.android.parcel.Parcelize
import java.util.HashSet

@Parcelize
data class SearchResult(
    var id: Long = 0,
    var url: String? = null,
    var searchWord: String? = null,
    var matches: Int? = 0,
    var linkSet: HashSet<String> = HashSet(),
    @Keys.Companion.SearchExecutionFlag var executionResultStatus: Int? = -1,
    val error: QueryError = QueryError()
): Parcelable