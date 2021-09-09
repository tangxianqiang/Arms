package com.blcodes.mvvm_framework.repo.sp

import com.tencent.mmkv.MMKV

class SPRepository {
    companion object {
        private val mLock = Any()

        @Volatile
        private var mInstance: SPRepository? = null

        fun getInstance(): SPRepository {
            if (mInstance != null) {
                return mInstance!!
            }
            synchronized(mLock) {
                if (mInstance == null) {
                    mInstance = SPRepository()
                }
                return mInstance!!
            }
        }
    }

    fun getDefault(): MMKV {
        return MMKV.defaultMMKV()
    }

    fun getMMKVBYProcess(): MMKV? {
        return MMKV.mmkvWithID("PWDManager", MMKV.MULTI_PROCESS_MODE)
    }
}