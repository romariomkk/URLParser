package com.romariomkk.urltextparser.view.main

import android.view.View
import com.romariomkk.urltextparser.R
import com.romariomkk.urltextparser.core.pojo.SearchResult
import com.romariomkk.urltextparser.databinding.ItemResultBinding
import com.romariomkk.urltextparser.util.Keys
import com.romariomkk.urltextparser.view.base.AbsRVAdapter
import com.romariomkk.urltextparser.view.base.AbsRVViewHolder

class ResultsAdapter:
    AbsRVAdapter<SearchResult, ItemResultBinding, ResultsAdapter.ResultViewHolder>() {

    override fun provideLayoutId(viewType: Int) = R.layout.item_result

    override fun getViewHolder(view: View, viewType: Int) = ResultViewHolder(view)

    fun process(searchResult: SearchResult) {
        when (searchResult.executionResultStatus) {
            Keys.FLAG_CREATED -> {
                add(searchResult)
            }
            else -> {
                notifyItemChanged(getItemPosition(searchResult), searchResult)
            }
        }
    }

    class ResultViewHolder(view: View): AbsRVViewHolder<SearchResult, ItemResultBinding>(view) {
        override fun bind(item: SearchResult?, payloads: MutableList<Any>?) {
            super.bind(item, payloads)
            if (payloads?.isNotEmpty()!! && payloads[0] is SearchResult) {
                val searchResult = payloads[0] as SearchResult
                binding?.result = searchResult
            } else {
                binding?.result = item
            }
            binding?.executePendingBindings()
        }
    }
}