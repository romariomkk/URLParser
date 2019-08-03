package com.romariomkk.urltextparser.view.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.romariomkk.urltextparser.util.annotation.AnnotationUtil

abstract class AbsActivity<DB : ViewDataBinding, VM : AbsViewModel> : AppCompatActivity() {

    protected lateinit var binding: DB
    var viewModel: VM? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, AnnotationUtil.findViewId(this))
        setViewModel()
    }

    private fun setViewModel() {
        if (AnnotationUtil.hasViewModel(this)) {
            val viewModelClass = AnnotationUtil.findViewModelClass(this)
            viewModel = (ViewModelProviders.of(this).get(viewModelClass) as VM)
                .apply {
                    onAttached()
                }
        }
    }

    fun <T> AbsViewModel.reObserve(liveData: LiveData<T>, observer: Observer<T>) {
        liveData.removeObserver(observer)
        liveData.observe(this@AbsActivity, observer)
    }
}