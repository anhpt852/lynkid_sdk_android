package vn.linkid.sdk.home.repository

import android.util.Log
import vn.linkid.sdk.models.category.Category
import vn.linkid.sdk.models.category.HomeCategoryResponseModel
import vn.linkid.sdk.models.banner.HomeNewsAndBannerModel
import vn.linkid.sdk.models.member.Member
import vn.linkid.sdk.models.member.MemberResponseModel
import vn.linkid.sdk.models.point.Point
import vn.linkid.sdk.models.point.PointResponseModel
import vn.linkid.sdk.home.service.HomeService
import vn.linkid.sdk.models.gift.HomeGiftGroupResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.models.flash_sale.GetAllFlashSaleProgramResponseModel

class HomeRepository(private val service: HomeService) {

    suspend fun getMemberInfo(): Flow<Result<Member>> =
        service.getMemberInfo().map { result ->
            if (result.isSuccess) {
                val memberResponseModel: MemberResponseModel? = result.getOrNull()
                if (memberResponseModel?.data != null && memberResponseModel.isSuccess == true) {
                    LynkiD_SDK.memberId = memberResponseModel.data.id ?: 0
                    Result.success(memberResponseModel.data)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d("HomeRepository", "getMemberInfo: ${result.exceptionOrNull()?.toString()}")
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    suspend fun getPointInfo(): Flow<Result<Point>> =
        service.getPointInfo().map { result ->
            if (result.isSuccess) {
                val pointResponseModel: PointResponseModel? = result.getOrNull()
                if (pointResponseModel?.data != null && pointResponseModel.isSuccess == true) {
                    LynkiD_SDK.memberId = pointResponseModel.data.items?.id ?: 0
                    Result.success(pointResponseModel.data.items!!)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d("HomeRepository", "getPointInfo: ${result.exceptionOrNull()?.toString()}")
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    suspend fun getBannerAndNews(): Flow<Result<HomeNewsAndBannerModel>> =
        service.getBannerAndNews().map { result ->
            if (result.isSuccess) {
                Result.success(result.getOrNull()!!)
            } else {
                Log.d("HomeRepository", "getBannerAndNews: ${result.exceptionOrNull()?.toString()}")
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    suspend fun getHomeCategories(): Flow<Result<List<Category>>> =
        service.getHomeCategories().map { result ->
            if (result.isSuccess) {
                val homeCategoryResponseModel: HomeCategoryResponseModel? = result.getOrNull()
                if (homeCategoryResponseModel?.data?.row2 != null) {
                    Result.success(homeCategoryResponseModel.data.row2)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "HomeRepository",
                    "getHomeCategories: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    suspend fun getHomeGiftGroup(): Flow<Result<HomeGiftGroupResponseModel>> =
        service.getHomeGiftGroup().map { result ->
            if (result.isSuccess) {
                val homeGiftGroupResponseModel: HomeGiftGroupResponseModel? = result.getOrNull()
                if (homeGiftGroupResponseModel != null) {
                    Result.success(homeGiftGroupResponseModel)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "HomeRepository",
                    "getHomeGiftGroup: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    suspend fun getAllFlashSaleProgram(): Flow<Result<GetAllFlashSaleProgramResponseModel>> =
        service.getAllFlashSaleProgram().map { result ->
            if (result.isSuccess) {
                val getAllFlashSaleProgramResponseModel: GetAllFlashSaleProgramResponseModel? =
                    result.getOrNull()
                if (getAllFlashSaleProgramResponseModel != null) {
                    Result.success(getAllFlashSaleProgramResponseModel)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "HomeRepository",
                    "getAllFlashSaleProgram: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }
}