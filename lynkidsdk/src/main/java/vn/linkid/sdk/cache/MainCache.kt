package vn.linkid.sdk.cache

object MainCache {
    private var cache: MutableMap<String, Pair<Long, Any>> = mutableMapOf()

    private const val CACHE_DURATION = 300000 // 5 minutes in milliseconds

    fun put(key: String, value: Any) {
        cache[key] = System.currentTimeMillis() to value
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
}