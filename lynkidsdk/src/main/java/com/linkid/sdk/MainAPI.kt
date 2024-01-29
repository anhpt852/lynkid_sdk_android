package com.linkid.sdk

import com.linkid.sdk.models.category.HomeCategoryResponseModel
import com.linkid.sdk.models.banner.HomeNewsAndBannerModel
import com.linkid.sdk.models.gift.HomeGiftGroupResponseModel
import com.linkid.sdk.models.member.MemberResponseModel
import com.linkid.sdk.models.point.PointResponseModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

val logging: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}
val client: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${LynkiD_SDK.token}")
            .build()
        chain.proceed(newRequest)
    })
    .addInterceptor(logging)
    .build()
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://vpid-mobile-api-uat.linkid.vn")
//    .baseUrl("https://vpid-mobile-api.linkid.vn")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
val mainAPI: APIEndpoints = retrofit.create(APIEndpoints::class.java)

interface APIEndpoints {
    @GET("api/Member/View")
    suspend fun getMemberInfo(@Query("memberCode") memberCode: String = LynkiD_SDK.memberCode): MemberResponseModel

    @GET("api/Member/ViewPoint")
    suspend fun getPointInfo(@Query("memberCode") memberCode: String = LynkiD_SDK.memberCode): PointResponseModel

    @GET("api/Article/GetAllArticleAndRelatedNews_Optimize")
    suspend fun getBannerAndNews(@QueryMap queries: MutableMap<String, Any>): HomeNewsAndBannerModel

    @GET("api/GiftCategory/GiftListCategoriesInTwoRows")
    suspend fun getHomeCategories(@Query("memberCode") memberCode: String = LynkiD_SDK.memberCode): HomeCategoryResponseModel

    @GET("api/GiftInfos/appv1dot1/get-gift-group-for-home-page")
    suspend fun getHomeGiftGroup(@Query("memberCode") memberCode: String = LynkiD_SDK.memberCode): HomeGiftGroupResponseModel
}