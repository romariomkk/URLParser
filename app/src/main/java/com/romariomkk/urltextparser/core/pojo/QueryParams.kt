package com.romariomkk.urltextparser.core.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QueryParams(
    var url: String = "",
    var searchText: String = "",
    var maxThreads: Int = 0,
    var maxURLs: Int = 0
): Parcelable