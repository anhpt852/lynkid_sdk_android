package vn.linkid.sdk.search.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.linkid.sdk.models.gift.Gift
import vn.linkid.sdk.search.adapter.SearchPagingSource
import vn.linkid.sdk.search.service.SearchService

class SearchRepository(private val service: SearchService) {

    fun getGiftsStream(keyword: String): Flow<PagingData<Gift>> = Pager(
        PagingConfig(pageSize = 10, enablePlaceholders = false)
    ) {
        SearchPagingSource(service, keyword)
    }.flow

}