package vn.linkid.sdk.gift_usage.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import vn.linkid.sdk.all_gift.service.AllGiftService
import vn.linkid.sdk.models.category.Category
import vn.linkid.sdk.models.category.GiftCategoryResponseModel
import vn.linkid.sdk.models.gift.AllGiftGroupResponseModel
import vn.linkid.sdk.models.gift.HomeGiftGroup
import vn.linkid.sdk.models.gift.HomeGiftGroupResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.all_gift.paging.GiftGroupPagingSource
import vn.linkid.sdk.all_gift.service.GiftGroupService
import vn.linkid.sdk.category.paging.CategoryPagingSource
import vn.linkid.sdk.gift_usage.paging.GiftUsagePagingSource
import vn.linkid.sdk.gift_usage.service.GiftUsageService
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.models.category.GiftFilterModel
import vn.linkid.sdk.models.category.HomeCategoryResponseModel
import vn.linkid.sdk.models.gift_usage.GiftUsageAddress

class GiftUsageRepository(private val service: GiftUsageService) {

    fun getGiftUsageStream(giftCode: String, name: String?): Flow<PagingData<GiftUsageAddress>> =
        Pager(
            PagingConfig(pageSize = 10, enablePlaceholders = false)
        ) {
            GiftUsagePagingSource(service, giftCode, name)
        }.flow
}