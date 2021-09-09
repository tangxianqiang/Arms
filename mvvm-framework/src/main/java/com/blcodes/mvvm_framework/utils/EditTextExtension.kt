package com.blcodes.mvvm_framework.utils

import android.text.InputFilter
import android.text.Spanned
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import java.util.regex.Matcher
import java.util.regex.Pattern


fun EditText.setEnterOrSearchActionListener(callback: (() -> Unit?)?) {
    this.setOnEditorActionListener(object : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                event?.keyCode == KeyEvent.KEYCODE_ENTER ||
                actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_NEXT) {
                callback?.invoke()
                return true
            }
            return false
        }

    })
}

fun EditText.setFilters(max: Int = 300, needSpe: Boolean = false, rootView: View? = null) {
    val inputFilter: InputFilter = object : InputFilter {
        var pattern: Pattern = Pattern.compile(
            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\ud83e\udc00-\ud83e\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE
        )
        var patternSpe: Pattern = Pattern.compile(
            "[`~!% #\$^&*()=|{}':;',\\\\[\\\\]<>/?~！#￥……&*（）——|{}【】‘；：”“'。，、？_]",
            Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE
        )

        override fun filter(
            charSequence: CharSequence,
            i: Int,
            i1: Int,
            spanned: Spanned,
            i2: Int,
            i3: Int
        ): CharSequence {
            val matcher: Matcher = pattern.matcher(charSequence)
//            val matcherSpe: Matcher = patternSpe.matcher(charSequence)
//            if (needSpe) {
//                if (matcherSpe.find()) {
//                    rootView?.let {
//                        if (context is IBaseActivity) {
//                            (context as IBaseActivity).showSnackbar(rootView, "不支持特殊符号")
//                        }
//                    }
//                    return ""
//                }
//            }
            return if (matcher.find()) {
                rootView?.let {
                    context.showToast("暂不支持输入表情")
                }
                ""
            } else {
                charSequence
            }
        }
    }

    this.filters = arrayOf(inputFilter, InputFilter.LengthFilter(max))
}