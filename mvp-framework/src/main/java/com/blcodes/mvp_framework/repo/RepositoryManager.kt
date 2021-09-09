package com.blcodes.mvp_framework.repo

import java.util.*

/**
 * Singleton manager,used in presenter. Never operate data in other place besides repo
 * <p> There are four kinds of data can be operate.
 *     1.Local file
 *     2.SharedPreference
 *     3.SQLite
 *     4.net data
 * </p>
 */
class RepositoryManager {
    /* The repo entry will be released by gc.Use map to cache the repos */
    private val repositories = WeakHashMap<String, IRepository>()

    companion object {
        private val mLock = Any()

        @Volatile
        private var mInstance: RepositoryManager? = null

        fun getInstance(): RepositoryManager {
            if (mInstance != null) {
                return mInstance!!
            }
            synchronized(mLock) {
                if (mInstance == null) {
                    mInstance = RepositoryManager()
                }
                return mInstance!!
            }
        }
    }

    /**
     * Get right repository for user
     */
    @Synchronized
    fun <T : IRepository> dispatchRepository(clazz: Class<T>): T? {
        return if (repositories.contains(clazz.name)) {
            repositories[clazz.name] as T
        } else {
            val repo = clazz.newInstance()
            repositories[clazz.name] = repo
            repo
        }
    }

    fun getRepoSize() = repositories.size
}
