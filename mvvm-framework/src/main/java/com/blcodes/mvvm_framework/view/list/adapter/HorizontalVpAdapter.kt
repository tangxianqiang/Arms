package com.blcodes.mvvm_framework.view.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.blcodes.mvvm_framework.R


class HorizontalVpAdapter private constructor(
    private val context: Context,
    private val layout: Int?,
    private val onBindItem: OnBindItem?,
) : RecyclerView.Adapter<HorizontalVpAdapter.ViewHolder?>() {
    val mLooperCount = 3
    private var data: List<Any>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mLayout = if (viewType == 0) {
            R.layout.item_recycler_empty
        } else {
            viewType
        }

        return ViewHolder(
            LayoutInflater.from(context).inflate(mLayout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.binding == null) return

        getItem(position)?.let { onBindItem?.onBind(holder.binding, it) }
        holder.binding.setVariable(BR.data, getItem(position))
    }

    override fun getItemCount(): Int {
        // 无限滑动
        return getRealCount() * mLooperCount
    }

    private fun getRealCount(): Int {
        return data?.size?:0
    }

    fun getStartItem(): Int {
        if (getRealCount() == 0) {
            return 0
        }
        // 我们设置当前选中的位置为Integer.MAX_VALUE / 2,这样开始就能往左滑动
        // 但是要保证这个值与getRealPosition 的 余数为0，因为要从第一页开始显示
        var currentItem: Int = mLooperCount / 2
        if (currentItem % getRealCount() == 0) {
            return currentItem
        }
        // 直到找到从0开始的位置
        while (currentItem % getRealCount() != 0) {
            currentItem++
        }
        return currentItem
    }


    override fun getItemViewType(position: Int): Int {
        return layout ?: 0
    }


    fun getItem(position: Int): Any? {
        return if(data != null) data!![position % data!!.size] else null
    }

    fun setData(data: List<Any>?) {
        this.data = data
        notifyDataSetChanged()
    }

    interface OnBindItem {
        fun onBind(binding: ViewDataBinding, data: Any)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ViewDataBinding>(view)
    }

    companion object {
        @JvmStatic
        fun ViewPager2.getOrCreateAdapter(
            context: Context, @LayoutRes layout: Int? = null,
            onBindItem: OnBindItem? = null
        ): HorizontalVpAdapter {
            if (adapter !is HorizontalVpAdapter) {
                adapter = HorizontalVpAdapter(context, layout, onBindItem)
            }
            return adapter as HorizontalVpAdapter
        }
    }
}