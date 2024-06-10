package vn.linkid.sdk.gift_detail.cache

import vn.linkid.sdk.models.gift.GiftDetailResponseModel

class GiftDetailCache {

    companion object {
        // Cache duration in milliseconds, e.g., 5 minutes
        private const val CACHE_DURATION = 300000


        private var cache: MutableMap<Int, Pair<Long, GiftDetailResponseModel>> = mutableMapOf()

        fun put(id: Int, response: GiftDetailResponseModel) {
            cache[id] = System.currentTimeMillis() to response
        }

        fun get(id: Int): GiftDetailResponseModel? {
            val cached = cache[id]
            cached?.let {
                // Check if cache is still valid
                if (System.currentTimeMillis() - it.first < Companion.CACHE_DURATION) {
                    return it.second
                }
            }
            return null
        }

        fun clear() {
            cache.clear()
        }
    }
}