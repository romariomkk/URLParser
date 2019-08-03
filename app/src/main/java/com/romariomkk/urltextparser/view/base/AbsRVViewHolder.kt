package com.romariomkk.urltextparser.view.base

import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class AbsRVViewHolder<T, DB: ViewDataBinding>(
    override val containerView: View)
    : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private var clickListener: OnItemClickListener<T>? = null
    protected val binding: DB? = DataBindingUtil.bind(itemView)
    protected var item: T? = null
        private set


    @CallSuper
    open fun bind(item: T?, payloads: MutableList<Any>? = null) {
        this.item = item
    }

    init {
        itemView.setOnClickListener {
            clickListener?.onItemClicked(item!!)
        }
    }

    internal fun setOnClickListener(listener: OnItemClickListener<T>) {
        this.clickListener = listener
    }
}


interface OnItemClickListener<T> {
    fun onItemClicked(item: T)
}