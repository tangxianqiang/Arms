package com.blcodes.mvvm_framework.view.bingding

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blcodes.mvvm_framework.R
import com.blcodes.mvvm_framework.utils.OnSingleClickListener
import com.blcodes.mvvm_framework.utils.base64ToBitmap
import com.blcodes.mvvm_framework.utils.isBase64
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.launch

object Binding {
    @JvmStatic
    @BindingAdapter("src")
    fun bindImage(view: ImageView, url: String?) {
        if (url.isBase64()) {
            if (view.context is LifecycleOwner) {
                (view.context as LifecycleOwner).lifecycleScope.launch {
                    val bitmap = url.base64ToBitmap()
                    Glide.with(view).load(bitmap).into(view)
                }
            }
        } else {
            Glide.with(view).load(url).into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("src")
    fun bindImage(view: ImageView, src: Drawable) {
        Glide.with(view).load(src).into(view)
    }

    @JvmStatic
    @BindingAdapter("src")
    fun bindImage(view: ImageView, @RawRes @DrawableRes src: Int) {
        Glide.with(view).load(src).into(view)
    }

    @JvmStatic
    @BindingAdapter("src", "placeholder")
    fun bindImage(view: ImageView, url: String?, @RawRes @DrawableRes placeholder: Int) {
        if (url.isBase64()) {
            if (view.context is LifecycleOwner) {
                (view.context as LifecycleOwner).lifecycleScope.launch {
                    val bitmap = url.base64ToBitmap()
                    Glide.with(view).load(bitmap).placeholder(placeholder).into(view)
                }
            }
        } else {
            Glide.with(view).load(url).placeholder(placeholder).into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("src", "placeholder", "error", requireAll = false)
    fun bindImage(view: ImageView, url: String?, placeholder: Drawable?, error: Drawable?) {
        if (url.isBase64()) {
            if (view.context is LifecycleOwner) {
                (view.context as LifecycleOwner).lifecycleScope.launch {
                    val bitmap = url.base64ToBitmap()
                    Glide.with(view).load(bitmap).placeholder(placeholder).error(error
                        ?: placeholder).into(view)
                }
            }
        } else {
            Glide.with(view).load(url).placeholder(placeholder).error(error
                ?: placeholder).into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("visible")
    fun bindVisible(view: View, visible: Boolean?) {
        view.visibility = if (visible == false) View.GONE else View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter("invisible")
    fun bindInVisible(view: View, visible: Boolean?) {
        view.visibility = if (visible == false) View.INVISIBLE else View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter("onSingleClick")
    fun bindSingleClick(view: View, listener: OnSingleClickListener?) {
        view.setOnClickListener(listener)
    }

    @JvmStatic
    @BindingAdapter("onLongClick")
    fun bindLongClick(view: View, listener: View.OnClickListener?) {
        view.isLongClickable = true
        if (listener != null) {
            view.setOnLongClickListener {
                listener.onClick(it)

                true
            }
        }
    }

    @JvmStatic
    @BindingAdapter("onSingleClick", "clickDelay")
    fun bindSingleClick(view: View, listener: OnSingleClickListener?, delay: Int) {
        view.setOnClickListener(listener.apply {
            this?.delay = delay
        })
    }

    @JvmStatic
    @BindingAdapter("refreshing")
    fun bindRefreshing(view: SwipeRefreshLayout, isRefreshing: Boolean?) {
        if (isRefreshing != null) {
            view.isRefreshing = isRefreshing
        }
    }

    @JvmStatic
    @BindingAdapter("onRefresh")
    fun bindOnRefresh(view: SwipeRefreshLayout, listener: SwipeRefreshLayout.OnRefreshListener) {
        view.apply {
            setOnRefreshListener(listener)

            // 设置圈圈颜色为当前主题色
            setColorSchemeColors(TypedValue().let {
                view.context.theme.resolveAttribute(R.attr.colorPrimary, it, true)
                it.data
            })
        }
    }

    @JvmStatic
    @BindingAdapter("onSwitch")
    fun bindOnSwitch(view: SwitchCompat, listener: CompoundButton.OnCheckedChangeListener) {
        view.setOnCheckedChangeListener(listener)
    }

    @JvmStatic
    @BindingAdapter("onChecked")
    fun bindOnCheckBox(view: CheckBox, listener: CompoundButton.OnCheckedChangeListener?) {
        if (listener != null) view.setOnCheckedChangeListener(listener)
    }

    @JvmStatic
    @BindingAdapter("linkable")
    fun bindLinkable(view: TextView, linkable: Boolean) {
        if (linkable) {
            view.paint.apply {
                flags = Paint.UNDERLINE_TEXT_FLAG
                isAntiAlias = true
            }
        }
    }

    @JvmStatic
    @BindingAdapter("sex", "manImage", "womanImage", requireAll = false)
    fun setImageUrl(
        view: ImageView,
        sex: String,
        manImage: Drawable,
        womanImage: Drawable
    ) {
        when (sex) {
            "女" -> view.background = womanImage
            else -> view.background = manImage
        }
    }

    @JvmStatic
    @BindingAdapter("imageUrl", "circle", "sex", "manImage", "womanImage")
    fun setImageUrl(
        view: ImageView,
        url: String,
        circle: Boolean,
        sex: String,
        manImage: Drawable,
        womanImage: Drawable
    ) {
        val glid = Glide.with(view.context).load(url)
        when (sex) {
            "女" -> glid.placeholder(womanImage).error(womanImage)
            else -> glid.placeholder(manImage).error(manImage)
        }
        if (circle) glid.apply(RequestOptions.bitmapTransform(CircleCrop())) // 是否设置圆形图
        view.scaleType = ImageView.ScaleType.FIT_XY
        glid.into(view)
    }
}