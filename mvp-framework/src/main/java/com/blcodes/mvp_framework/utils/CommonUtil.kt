package com.blcodes.mvp_framework.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context


/**
 * 复制内容到剪贴板
 */
fun Context.copyContentToClipboard(content: String?) {
    val cm: ClipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val mClipData = ClipData.newPlainText("Label", content)
    cm.setPrimaryClip(mClipData)
}