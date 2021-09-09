package com.blcodes.mvvm_framework.view.bingding

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.blcodes.mvvm_framework.view.list.adapter.DataBindingRecyclerAdapter
import com.blcodes.mvvm_framework.view.list.adapter.DataBindingRecyclerAdapter.Companion.getOrCreateAdapter
import com.blcodes.mvvm_framework.view.list.adapter.HorizontalVpAdapter
import com.blcodes.mvvm_framework.view.list.adapter.HorizontalVpAdapter.Companion.getOrCreateAdapter
import com.blcodes.mvvm_framework.view.list.adapter.IRecyclerItem

object RecyclerBinding {
    @JvmStatic
    @BindingAdapter("onLoadMore", "noMore", requireAll = false)
    fun bindLoadMore(view: RecyclerView, onLoadMore: () -> Unit, noMore: Boolean = false) {
        view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // 1代表底部，-1代表顶部
                if (!recyclerView.canScrollVertically(1) && !noMore) {
                    onLoadMore()
                }
            }
        })
    }

    @JvmStatic
    @BindingAdapter("itemLayout", "onBindItem", "items", requireAll = false)
    fun bindViewPagerAdapter(
        view: ViewPager2,
        layout: Int,
        onBindItem: HorizontalVpAdapter.OnBindItem?,
        items: List<Any>?
    ) {
        val adapter = view.getOrCreateAdapter(view.context, layout, onBindItem)
        adapter.setData(items)
        if (items != null && view.currentItem == 0) {
            view.currentItem = adapter.getStartItem()
        }
    }

    @JvmStatic
    @BindingAdapter("itemLayout")
    fun bindRecyclerAdapter(view: RecyclerView, layout: Int) {
        bindRecyclerAdapter(view, layout, null, null)
    }

    @JvmStatic
    @BindingAdapter("itemLayout", "onBindItem")
    fun bindRecyclerAdapter(
        view: RecyclerView,
        layout: Int,
        onBindItem: DataBindingRecyclerAdapter.OnBindItem
    ) {
        bindRecyclerAdapter(view, layout, onBindItem, null)
    }

    @JvmStatic
    @BindingAdapter("itemLayout", "items")
    fun bindRecyclerAdapter(
        view: RecyclerView,
        layout: Int,
        items: List<Any>?,
    ) {
        bindRecyclerAdapter(view, layout, null, items)
    }

    @JvmStatic
    @BindingAdapter("itemLayout", "onBindItem", "items")
    fun bindRecyclerAdapter(
        view: RecyclerView,
        layout: Int,
        onBindItem: DataBindingRecyclerAdapter.OnBindItem?,
        items: List<Any>?,
    ) {
        val adapter = view.getOrCreateAdapter(view.context, layout, onBindItem)

        // 更新数据
        if (checkItemType(items)) {
            // 如果旧数据为空，就执行动画
            val isOldInValid = adapter.itemCount == 0
            adapter.submitList(items?.toList())

            if (isOldInValid) {
                view.scheduleLayoutAnimation()
            }
        } else {
            adapter.submitList(items?.toList()) {
                adapter.notifyDataSetChanged()
                view.scheduleLayoutAnimation()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("linkers")
    fun bindRecyclerAdapter(view: RecyclerView, linkers: List<DataBindingRecyclerAdapter.Linker>) {
        bindRecyclerAdapter(view, linkers, null, null)
    }

    @JvmStatic
    @BindingAdapter("linkers", "onBindItem")
    fun bindRecyclerAdapter(
        view: RecyclerView,
        linkers: List<DataBindingRecyclerAdapter.Linker>,
        onBindItem: DataBindingRecyclerAdapter.OnBindItem,
    ) {
        bindRecyclerAdapter(view, linkers, onBindItem, null)
    }

    @JvmStatic
    @BindingAdapter("linkers", "onBindItem")
    fun bindRecyclerAdapter(
        view: RecyclerView,
        linkers: List<DataBindingRecyclerAdapter.Linker>,
        items: List<Any>?,
    ) {
        bindRecyclerAdapter(view, linkers, null, items)
    }

    @JvmStatic
    @BindingAdapter("linkers", "onBindItem", "items")
    fun bindRecyclerAdapter(
        view: RecyclerView,
        linkers: List<DataBindingRecyclerAdapter.Linker>,
        onBindItem: DataBindingRecyclerAdapter.OnBindItem?,
        items: List<Any>?,
    ) {
        val adapter =
            view.getOrCreateAdapter(view.context, onBindItem = onBindItem, linkers = linkers)

        // 更新数据
        if (checkItemType(items)) {
            // 如果旧数据为空，就执行动画
            val isOldInValid = adapter.itemCount == 0
            adapter.submitList(items?.toList())

            if (isOldInValid) {
                view.scheduleLayoutAnimation()
            }
        } else {
            adapter.submitList(items?.toList()) {
                adapter.notifyDataSetChanged()
                view.scheduleLayoutAnimation()
            }
        }
    }

    private fun <T> checkItemType(items: List<T>?): Boolean {
        if (items.isNullOrEmpty()) return false

        val typeRight = items.all { it is IRecyclerItem }
        // 当items不为IRecyclerItem类型时给出建议
        if (!typeRight) {
            Log.w(
                "RecyclerAdapter错误：",
                "\n\n============================================\n\n" +
                        "item的类型不太准确，建议在使用DataBindingRecyclerAdapter时，传递的List的类型为List<T : IRecyclerItem>" +
                        "\n\n============================================\n"
            )
        }

        return typeRight
    }
}