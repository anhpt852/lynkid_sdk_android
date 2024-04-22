package vn.linkid.sdk.home.service

import android.util.Log
import vn.linkid.sdk.APIEndpoints
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.models.category.HomeCategoryResponseModel
import vn.linkid.sdk.models.banner.HomeNewsAndBannerModel
import vn.linkid.sdk.models.gift.HomeGiftGroupResponseModel
import vn.linkid.sdk.models.member.MemberResponseModel
import vn.linkid.sdk.models.point.PointResponseModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow

class HomeService(private val api: APIEndpoints) {

    suspend fun getMemberInfo(): Flow<Result<MemberResponseModel>> = flow {
        Log.d("HomeService", "getMemberInfo: LynkiD_SDK.accessToken ${LynkiD_SDK.accessToken}")
        emit(
            Result.success(
                api.getMemberInfo()
            )
        )
    }.catch {
        Log.e("HomeService", "getMemberInfo: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getPointInfo(): Flow<Result<PointResponseModel>> = flow {
        emit(
            Result.success(
                api.getPointInfo()
            )
        )
    }.catch {
        Log.e("HomeService", "getPointInfo: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getBannerAndNews(): Flow<Result<HomeNewsAndBannerModel>> = flow {
        Log.d("HomeService", "getBannerAndNews: LynkiD_SDK.seedToken ${LynkiD_SDK.seedToken}")
        emit(
            Result.success(
                api.getBannerAndNews(
                    queries = mutableMapOf(
                        "SkipCount" to 0, "MaxResultCount" to 5, "Sorting" to "Article.Ordinal asc"
                    )
                )
            )
        )
    }.catch {
        Log.e("HomeService", "getBannerAndNews: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getHomeCategories(): Flow<Result<HomeCategoryResponseModel>> = flow {
        emit(
            Result.success(
                api.getHomeCategories()
            )
        )
    }.catch {
        Log.e("HomeService", "getHomeCategories: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getHomeGiftGroup(): Flow<Result<HomeGiftGroupResponseModel>> = flow {
        emit(
            Result.success(
                api.getHomeGiftGroup()
            )
        )
    }.catch {
        Log.e("HomeService", "getHomeGiftGroup: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}