package vn.linkid.sdk

import android.util.Log
import vn.linkid.sdk.models.auth.AuthToken
import vn.linkid.sdk.models.auth.ConnectedMemberAuthToken
import vn.linkid.sdk.models.auth.MemberAuthToken
import vn.linkid.sdk.models.category.HomeCategoryResponseModel
import vn.linkid.sdk.models.banner.HomeNewsAndBannerModel
import vn.linkid.sdk.models.category.GiftCategoryResponseModel
import vn.linkid.sdk.models.category.GiftsByCategoryResponseModel
import vn.linkid.sdk.models.gift.AllGiftGroupResponseModel
import vn.linkid.sdk.models.gift.HomeGiftGroupResponseModel
import vn.linkid.sdk.models.member.MemberResponseModel
import vn.linkid.sdk.models.point.PointResponseModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap
import vn.linkid.sdk.models.my_reward.MyRewardListResponseModel

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
    .addInterceptor(Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)
        val responseBody = response.peekBody(Long.MAX_VALUE)
        try{
            Log.d("Response Body", responseBody.string())
        } catch (e: Exception) {
            Log.e("Response Body", e.message ?: "empty")
        }
        response
    })
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
        @Body body: Map<String, String> = mapOf(
            "phoneNumber" to LynkiD_SDK.phoneNumber,
            "cif" to LynkiD_SDK.cif,
            "name" to LynkiD_SDK.name
        )
    ): AuthToken

    @POST("api/sdk-v1/check-member-and-connection")
    suspend fun checkMember(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ),
        @Body body: Map<String, String> = mapOf(
            "phoneNumber" to LynkiD_SDK.phoneNumber,
            "cif" to LynkiD_SDK.cif
        )
    ): ConnectedMemberAuthToken

    @POST("api/sdk-v1/authen-with-connected-phone")
    suspend fun authConnectedMember(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ),
        @Body body: Map<String, String> = mapOf(
            "originalPhone" to LynkiD_SDK.phoneNumber,
            "connectedPhone" to LynkiD_SDK.connectedPhoneNumber
        )
    ): MemberAuthToken

    @POST("api/sdk-v1/create-member")
    suspend fun createMember(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ),
        @Body body: Map<String, String> = mapOf(
            "phoneNumber" to LynkiD_SDK.phoneNumber,
            "cif" to LynkiD_SDK.cif
        )
    ): ConnectedMemberAuthToken

    @GET("api/sdk-v1/Member/View")
    suspend fun getMemberInfo(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ), @Query("MemberCode") memberCode: String = LynkiD_SDK.memberCode
    ): MemberResponseModel

    @GET("api/sdk-v1/Member/View-point")
    suspend fun getPointInfo(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ), @Query("MemberCode") memberCode: String = LynkiD_SDK.memberCode
    ): PointResponseModel

    @GET("api/sdk-v1/get-all-article-and-related-news")
    suspend fun getBannerAndNews(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): HomeNewsAndBannerModel

    @GET("api/sdk-v1/get-list-categories")
    suspend fun getHomeCategories(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ), @Query("MemberCode") memberCode: String = LynkiD_SDK.memberCode
    ): HomeCategoryResponseModel

    @GET("api/sdk-v1/get-gift-group")
    suspend fun getHomeGiftGroup(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ), @Query("MemberCode") memberCode: String = LynkiD_SDK.memberCode
    ): HomeGiftGroupResponseModel

    @GET("api/GiftCategory/GiftListCategories_v1")
    suspend fun getGiftCategories(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): GiftCategoryResponseModel

    @GET("api/sdk-v1/get-gift-all-infors")
    suspend fun getAllGiftGroups(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): AllGiftGroupResponseModel

    @GET("api/sdk-v1/get-all-by-member-code")
    suspend fun getGiftsByCategory(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): GiftsByCategoryResponseModel

    @GET("/api/sdk-v1/GetAllWithEGift")
    suspend fun getMyRewards(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): MyRewardListResponseModel

    @GET("/api/GiftTransactions/GetAllWithEGift")
    suspend fun getMyRewardDetail(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): AllGiftGroupResponseModel
}