package com.romariomkk.urltextparser.view.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.romariomkk.urltextparser.R
import com.romariomkk.urltextparser.core.pojo.SearchResult
import com.romariomkk.urltextparser.databinding.ActivityMainBinding
import com.romariomkk.urltextparser.util.Keys
import com.romariomkk.urltextparser.util.Resource
import com.romariomkk.urltextparser.util.annotation.RequiresView
import com.romariomkk.urltextparser.util.annotation.RequiresViewModel
import com.romariomkk.urltextparser.util.incProgress
import com.romariomkk.urltextparser.view.base.AbsActivity
import kotlinx.android.synthetic.main.activity_main.*

@RequiresView(R.layout.activity_main)
@RequiresViewModel(MainViewModel::class)
class MainActivity : AbsActivity<ActivityMainBinding, MainViewModel>() {

    private var mResultsAdapter = ResultsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel

        with(rvResults) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
            adapter = mResultsAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel?.let { vm ->
            vm.reObserve(vm.validationLiveData, mParamsValidityObserver)
            vm.reObserve(vm.searchResultSource, mSearchResultData)
        }
    }

    private val mParamsValidityObserver = Observer<ValidityResult> { validity ->
        resetErrors()
        when (validity) {
            ValidityResult.ERROR_RESULT_LIMIT -> {
                tilLimit.error = getString(R.string.error_limit)
            }
            ValidityResult.ERROR_URL -> {
                tilURL.error = getString(R.string.error_url)
            }
            ValidityResult.ERROR_SEARCH_WORD -> {
                tilSearch.error = getString(R.string.error_search)
            }
            ValidityResult.ERROR_MAX_THREADS -> {
                tilThreads.error = getString(R.string.error_threads)
            }
        }
    }

    //todo all happening in this method should be moved in favor of DataBinding
    private val mSearchResultData = Observer<Resource<SearchResult>> {
        when (it.status) {
            Resource.Status.START -> {
                tvSearchStatus.setText(R.string.start)
                pbSearch.run {
                    progress = 0
                    max = viewModel?.queryParams?.maxURLs!!
                }
                mResultsAdapter.clear()

                btnStart.visibility = View.GONE
                btnStop.visibility = View.VISIBLE
                btnPauseResume.run {
                    setText(R.string.pause)
                    visibility = View.VISIBLE
                }
            }
            Resource.Status.LOADING -> {
                it.data?.let { firstElement ->
                    mResultsAdapter.add(firstElement)
                }
            }
            Resource.Status.SUCCESS -> {
                val searchResult = it.data!!
                mResultsAdapter.process(searchResult)
                when (searchResult.executionResultStatus) {
                    Keys.FLAG_ERROR, Keys.FLAG_FINISHED -> {
                        updateProgress()
                    }
                }
            }
            Resource.Status.ERROR -> {
            }
            Resource.Status.PAUSE -> {
                btnPauseResume.setText(R.string.resume)
            }
            Resource.Status.RESUME -> {
                btnPauseResume.setText(R.string.pause)
            }
            Resource.Status.STOP -> {
                tvSearchStatus.setText(R.string.stopped)
                btnStart.visibility = View.VISIBLE
                btnStop.visibility = View.GONE
                btnPauseResume.visibility = View.GONE
            }
        }
    }

    private fun resetErrors() {
        for (view in arrayOf(tilLimit, tilURL, tilSearch, tilThreads)) {
            view.isErrorEnabled = false
        }
    }

    private fun updateProgress() {
        pbSearch.incProgress {
            btnStart.visibility = View.VISIBLE
            btnPauseResume.visibility = View.GONE
            btnStop.visibility = View.GONE
        }

        tvSearchStatus.text = StringBuilder()
            .append(pbSearch.progress).append(" of ").append(pbSearch.max).toString()
    }
}
