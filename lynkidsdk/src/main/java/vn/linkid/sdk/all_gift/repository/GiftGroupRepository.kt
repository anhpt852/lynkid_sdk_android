package vn.linkid.sdk.all_gift.repository

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
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.models.category.GiftFilterModel
import vn.linkid.sdk.models.category.HomeCategoryResponseModel

class GiftGroupRepository(private val service: GiftGroupService) {

    fun getGiftsStream(groupCode: String): Flow<PagingData<Gift>> =
        Pager(
            PagingConfig(pageSize = 10, enablePlaceholders = false)
        ) {
            GiftGroupPagingSource(service, groupCode)
        }.flow
}