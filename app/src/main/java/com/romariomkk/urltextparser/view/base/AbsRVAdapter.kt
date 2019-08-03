package com.romariomkk.urltextparser.view.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*


abstract class AbsRVAdapter<T, DB : ViewDataBinding, VH : AbsRVViewHolder<T, DB>>(
    private val itemClickListener: OnItemClickListener<T>? = null
) : RecyclerView.Adapter<VH>() {

    internal lateinit var context: Context
    private val items = ArrayList<T>()

    abstract fun provideLayoutId(viewType: Int): Int
    abstract fun getViewHolder(view: View, viewType: Int): VH

    private fun getView(parent: ViewGroup, viewType: Int): View {
        val view: View
        val layoutId = provideLayoutId(viewType)
        if (layoutId != -1) {
            view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        } else {
            throw IllegalStateException("No item layout specified for " + this.javaClass.name)
        }
        return view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        context = parent.context

        val viewHolder = getViewHolder(getView(parent, viewType), viewType)
        itemClickListener?.let {
            viewHolder.setOnClickListener(it)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(getItemAt(position))

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        holder.bind(getItemAt(position), payloads)
    }


    /* All boilerplate methods */

    override fun getItemCount() = items.size

    fun getItems() = items

    fun getItemPosition(item: T) = items.indexOf(item)

    fun getItemPosition(predicate: (T) -> Boolean) = items.indexOfFirst(predicate)

    fun getItemAt(position: Int): T? {
        return if (position < 0 || position >= items.size) null
        else items[position]
    }

    fun add(item: T) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun replace(item: T, predicate: (T) -> Boolean) {
        val index = items.indexOfFirst(predicate)
        replace(item, index)
    }

    fun replace(item: T, index: Int) {
        if (index < 0) return
        items[index] = item
        notifyItemChanged(index)
    }

    fun remove(item: T) {
        val position = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(position)
    }

    fun removeAt(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
    }

    fun removeBy(predicate: (T) -> Boolean) {
        remove(items.find(predicate) ?: return)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun isEmpty() = items.isEmpty()
}
