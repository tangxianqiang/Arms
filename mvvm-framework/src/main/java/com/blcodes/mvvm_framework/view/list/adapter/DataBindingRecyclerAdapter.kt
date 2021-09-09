package com.blcodes.mvvm_framework.view.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.blcodes.mvvm_framework.R

class DataBindingRecyclerAdapter private constructor(
    private val context: Context,
    private val layout: Int?,
    private val onBindItem: OnBindItem?,
    private val linkers: List<Linker>?
) : ListAdapter<Any, DataBindingRecyclerAdapter.ViewHolder>(RecyclerDiffCallback<Any>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mLayout = if (viewType == 0) {
            R.layout.item_recycler_empty
        } else {
            viewType
        }

        return ViewHolder(LayoutInflater.from(context).inflate(mLayout, parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isEmpty()) this.onBindViewHolder(holder, position)
        else {
            if(holder.binding == null) return
            payloads.forEach {
                onBindItem?.onBind(holder.binding, it, position)
                holder.binding.setVariable(BR.data, it)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.binding == null) return
        onBindItem?.onBind(holder.binding, getItem(position), position)
        holder.binding.setVariable(BR.data, getItem(position))
    }

    /**
     * 根据Linker，将layout作为ViewType传递
     */
    override fun getItemViewType(position: Int): Int {
        if (!linkers.isNullOrEmpty()) {
            return linkers.firstOrNull { it.compose(getItem(position)) }?.layout ?: 0
        }

        return layout ?: 0
    }

    interface OnBindItem {
        fun onBind(binding: ViewDataBinding, data: Any, position: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ViewDataBinding>(view)
    }

    companion object {
        @JvmStatic
        fun RecyclerView.getOrCreateAdapter(
            context: Context, @LayoutRes layout: Int? = null,
            onBindItem: OnBindItem? = null, linkers: List<Linker>? = null
        ): DataBindingRecyclerAdapter {
            if (adapter !is DataBindingRecyclerAdapter) {
                adapter = DataBindingRecyclerAdapter(context, layout, onBindItem, linkers).apply {
                    registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                            super.onItemRangeInserted(positionStart, itemCount)

                            (layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
                                positionStart, 0
                            )
                        }

                        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                            super.onItemRangeRemoved(positionStart, itemCount)

                            (layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
                                positionStart, 0
                            )
                        }
                    })
                }
            }

            return adapter as DataBindingRecyclerAdapter
        }
    }

    /**
     * RecyclerView多布局适配
     * 布局文件采用Linker的形式与其进行绑定
     * @param layout 布局文件
     * @param compose 采用该布局文件的前提因素
     */
    class Linker(@LayoutRes val layout: Int, val compose: (Any) -> Boolean)
}

/**
 * 数据更新时，采用该方案进行优化
 */
private class RecyclerDiffCallback<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return if (oldItem is IRecyclerItem && newItem is IRecyclerItem) {
            oldItem.getItemCompareTag() == newItem.getItemCompareTag()
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}

interface IRecyclerItem {
    fun getItemCompareTag(): Any?
}