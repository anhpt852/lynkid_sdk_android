package vn.linkid.sdk.cache

import android.util.Log

object MainCache {
    private var cache: MutableMap<String, Pair<Long, Any>> = mutableMapOf()

    private const val CACHE_DURATION = 300000 // 5 minutes in milliseconds

    fun put(key: String, value: Any) {
        if (isSuccessful(value)) {
            Log.d("MainCache", "put success: $key")
            cache[key] = System.currentTimeMillis() to value
        } else {
            Log.d("MainCache", "put failed: $key")
        }
    }

    fun <T> get(key: String): T? {
        val cached = cache[key]
        if (cached != null && (System.currentTimeMillis() - cached.first) < CACHE_DURATION) {
            @Suppress("UNCHECKED_CAST")
            return cached.second as? T
        }
        cache.remove(key)
        return null
    }

    fun clear() {
        cache.clear()
    }

    private fun isSuccessful(value: Any): Boolean {
        val isSuccess = value::class.members.find { it.name == "isSuccess" }?.call(value) as? Boolean
        val success = value::class.members.find { it.name == "success" }?.call(value) as? Boolean
        return isSuccess == true || success == true
    }
}