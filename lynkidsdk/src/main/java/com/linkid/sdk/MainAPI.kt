package com.linkid.sdk

import com.linkid.sdk.models.auth.AuthToken
import com.linkid.sdk.models.auth.ConnectedMemberAuthToken
import com.linkid.sdk.models.auth.MemberAuthToken
import com.linkid.sdk.models.category.HomeCategoryResponseModel
import com.linkid.sdk.models.banner.HomeNewsAndBannerModel
import com.linkid.sdk.models.category.GiftCategoryResponseModel
import com.linkid.sdk.models.gift.AllGiftGroupResponseModel
import com.linkid.sdk.models.gift.HomeGiftGroupResponseModel
import com.linkid.sdk.models.member.MemberResponseModel
import com.linkid.sdk.models.point.PointResponseModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

val logging: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}
val client: OkHttpClient = OkHttpClient.Builder()
//    .addInterceptor(Interceptor { chain ->
//        val newRequest = chain.request().newBuilder()
//            .addHeader("Authorization", "Bearer ${LynkiD_SDK.token}")
//            .build()
//        chain.proceed(newRequest)
//    })
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

    @POST("api/sdk-v1/partner/generate-token")
    suspend fun generateToken(
        @Header("X-PartnerCode") partnerCode: String = LynkiD_SDK.partnerCode,
        @Query("phoneNumber") phoneNumber: String = LynkiD_SDK.phoneNumber,
        @Query("cif") cif: String = LynkiD_SDK.cif,
        @Query("name") name: String = LynkiD_SDK.name
    ): AuthToken

    @POST("api/sdk-v1/check-member-and-connection")
    suspend fun checkMember(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ),
        @Query("phoneNumber") phoneNumber: String = LynkiD_SDK.phoneNumber,
        @Query("cif") cif: String = LynkiD_SDK.cif
    ): ConnectedMemberAuthToken

    @POST("api/sdk-v1/authen-with-connected-phone")
    suspend fun authConnectedMember(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ),
        @Query("originalPhone") phoneNumber: String = LynkiD_SDK.phoneNumber,
        @Query("connectedPhone") cif: String = LynkiD_SDK.connectedPhoneNumber
    ): MemberAuthToken

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

    @GET("/api/GiftCategory/GiftListCategories_v1")
    suspend fun getGiftCategories(@QueryMap queries: MutableMap<String, Any>): GiftCategoryResponseModel

    @GET("/api/GiftInfos/GetGiftAllInfors")
    suspend fun getAllGiftGroups(@QueryMap queries: MutableMap<String, Any>): AllGiftGroupResponseModel
}