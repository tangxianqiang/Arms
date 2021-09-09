package com.blcodes.mvvm_framework.model.sp

import com.blcodes.mvvm_framework.repo.sp.BoolDelegation
import com.blcodes.mvvm_framework.repo.sp.SPRepository

/**
 * Use sp like this.todo
 */
object SPHolder {
    var firstLaunch by BoolDelegation(SPRepository.getInstance().getDefault())
}