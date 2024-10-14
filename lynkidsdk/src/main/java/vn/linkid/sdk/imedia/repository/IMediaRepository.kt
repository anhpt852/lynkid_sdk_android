package vn.linkid.sdk.imedia.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.imedia.paging.IMediaHistoryTabPagingSource
import vn.linkid.sdk.imedia.service.IMediaService
import vn.linkid.sdk.models.imedia.GetIMediaHistory

class IMediaRepository(private val service: IMediaService) {

    suspend fun getBrandByVendor(tab: Int) = service.getBrandByVendor().map { result ->
        if (result.isSuccess) {
            val brandsByVendor = result.getOrNull()
            if (brandsByVendor != null && brandsByVendor.success == true) {
                val brands = brandsByVendor.result
                if (!brands.isNullOrEmpty()) {
                    val dataBrands = brands.filter {
                        it.brandMapping?.thirdPartyBrandId?.lowercase()?.contains("data") == true
                    }
                    val cardBrands = brands.toMutableList().apply {
                        removeAll(dataBrands)
                    }
                    Result.success(
                        if (tab < 3) {
                            cardBrands
                        } else {
                            dataBrands
                        }
                    )
                }
                Result.success(brandsByVendor.result!!)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        } else {
            Log.d("iMediaRepository", "getBrandByVendor: ${result.exceptionOrNull()?.toString()}")
            Result.failure(result.exceptionOrNull()!!)
        }
    }

    suspend fun getGiftsByGroupType(groupType: Int) =
        service.getGiftsByGroupType(groupType).map { result ->
            if (result.isSuccess) {
                val giftsByGroupType = result.getOrNull()
                if (giftsByGroupType != null && giftsByGroupType.success == true) {
                    Result.success(giftsByGroupType.result!!)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "iMediaRepository",
                    "getGiftsByGroupType: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    suspend fun getAllIMedia(brandId: Int) =
        service.getAllIMedia(brandId).map { result ->
            if (result.isSuccess) {
                val iMediaGifts = result.getOrNull()
                if (iMediaGifts != null && iMediaGifts.success == true) {
                    Result.success(iMediaGifts.result!!)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "iMediaRepository",
                    "getAllIMedia: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    fun getIMediaHistoryStream(tab: Int): Flow<PagingData<GetIMediaHistory>> =
        Pager(
            PagingConfig(pageSize = 10, enablePlaceholders = false)
        ) {
            IMediaHistoryTabPagingSource(service, tab)
        }.flow
}