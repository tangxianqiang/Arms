package com.blcodes.mvvm_framework.utils

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object FragmentUtil {
    /**
     * 启动fragment并隐藏其他fragment
     */
    inline fun <reified T : Fragment> startFragment(
        fragment: T,
        manager: FragmentManager,
        @IdRes contentId: Int,
        tag: String? = null
    ) {
        val tran = manager.beginTransaction()
        if (manager.fragments.contains(fragment)) {
            if (!fragment.isVisible) {
                tran.show(fragment)
            } else {
                return
            }
        } else {
            tran.add(contentId, fragment, tag ?: fragment::class.java.name)
            tran.show(fragment)
        }

        // 隐藏其他界面，只显示当前的
        manager.fragments
            .filter { it != fragment }
            .forEach { tran.hide(it) }

        tran.commit()
    }
}