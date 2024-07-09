package vn.linkid.sdk.utils

import android.util.Log
import kotlinx.coroutines.runBlocking
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
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.models.address.AddressResponseModel
import vn.linkid.sdk.models.auth.RefreshToken
import vn.linkid.sdk.models.diamond.GetDiamondCategoryResponseModel
import vn.linkid.sdk.models.exchange.ExchangeResponseModel
import vn.linkid.sdk.models.gift.GiftDetailResponseModel
import vn.linkid.sdk.models.merchant.GetMerchantResponseModel
import vn.linkid.sdk.models.my_reward.MyRewardListResponseModel
import vn.linkid.sdk.models.transaction.GetTransactionDetailResponseModel
import vn.linkid.sdk.models.transaction.GetTransactionItem
import vn.linkid.sdk.models.transaction.GetTransactionResponseModel

val logging: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}
val client: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(logging)
    .addInterceptor(Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)
        val responseBody = response.peekBody(Long.MAX_VALUE)
        try {
            Log.d("Response Body", responseBody.string())
        } catch (e: Exception) {
            Log.e("Response Body", e.message ?: "empty")
        }
        if (response.code == 401) {
            synchronized(Any()) {
                val newToken = runBlocking { refreshAccessToken() }
                if (newToken != null) {
                    val newRequest = request.newBuilder()
                        .header("Authorization", "Bearer $newToken")
                        .build()
                    return@Interceptor chain.proceed(newRequest)
                }
            }
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

suspend fun refreshAccessToken(): String? {
    return try {
        val headers = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessRefreshToken}"
        )
        val response = mainAPI.refreshToken(headers)
        LynkiD_SDK.accessToken = response.accessToken ?: ""
        LynkiD_SDK.accessRefreshToken = response.refreshToken ?: ""
        response.accessToken
    } catch (e: Exception) {
        Log.e("Token Refresh Error", e.message ?: "Unknown error")
        null
    }
}

interface APIEndpoints {

    @POST(Endpoints.GENERATE_TOKEN)
    suspend fun generateToken(
        @Header("X-PartnerCode") partnerCode: String = LynkiD_SDK.partnerCode,
        @Body body: MutableMap<String, String> = mutableMapOf(
            "phoneNumber" to LynkiD_SDK.phoneNumber,
            "cif" to LynkiD_SDK.cif,
            "name" to LynkiD_SDK.name
        )
    ): AuthToken

    @POST(Endpoints.CHECK_MEMBER)
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

    @POST(Endpoints.AUTH_CONNECTED_MEMBER)
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

    @POST(Endpoints.CREATE_MEMBER)
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

    @GET(Endpoints.GET_MEMBER_INFO)
    suspend fun getMemberInfo(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ), @Query("MemberCode") memberCode: String = LynkiD_SDK.memberCode
    ): MemberResponseModel

    @GET(Endpoints.GET_POINT_INFO)
    suspend fun getPointInfo(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ), @Query("MemberCode") memberCode: String = LynkiD_SDK.memberCode
    ): PointResponseModel

    @GET(Endpoints.GET_BANNER_AND_NEWS)
    suspend fun getBannerAndNews(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): HomeNewsAndBannerModel

    @GET(Endpoints.GET_HOME_CATEGORIES)
    suspend fun getHomeCategories(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ), @Query("MemberCode") memberCode: String = LynkiD_SDK.memberCode
    ): HomeCategoryResponseModel

    @GET(Endpoints.GET_HOME_GIFT_GROUP)
    suspend fun getHomeGiftGroup(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ), @Query("MemberCode") memberCode: String = LynkiD_SDK.memberCode
    ): HomeGiftGroupResponseModel

    @GET(Endpoints.GET_GIFT_CATEGORIES)
    suspend fun getGiftCategories(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): GiftCategoryResponseModel

    @GET(Endpoints.GET_ALL_GIFT_GROUPS)
    suspend fun getAllGiftGroups(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): AllGiftGroupResponseModel

    @GET(Endpoints.GET_GIFTS_BY_CATEGORY)
    suspend fun getGiftsByCategory(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): GiftsByCategoryResponseModel

    @GET(Endpoints.GET_MY_REWARDS)
    suspend fun getMyRewards(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): MyRewardListResponseModel

    @GET(Endpoints.GET_GIFT_DETAILS)
    suspend fun getGiftDetails(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.seedToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): GiftDetailResponseModel

    @POST(Endpoints.CREATE_TRANSACTION)
    suspend fun createTransaction(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ),
        @Body body: MutableMap<String, Any>
    ): ExchangeResponseModel

    @POST(Endpoints.CONFIRM_TRANSACTION)
    suspend fun confirmTransaction(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ),
        @Body body: MutableMap<String, Any>
    ): ExchangeResponseModel

    @GET(Endpoints.GET_TRANSACTIONS)
    suspend fun getTransactions(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): GetTransactionResponseModel

    @GET(Endpoints.GET_TRANSACTION_DETAIL)
    suspend fun getTransactionDetail(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): GetTransactionDetailResponseModel

    @GET(Endpoints.GET_LOCATIONS)
    suspend fun getLocations(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): AddressResponseModel

    @GET(Endpoints.GET_GIFT_USAGE_ADDRESS)
    suspend fun getGiftUsageAddress(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    )

    @GET(Endpoints.GET_GIFTS)
    suspend fun searchGift(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): AllGiftGroupResponseModel

    @GET(Endpoints.GET_MERCHANT)
    suspend fun getMerchant(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        )
    ): GetMerchantResponseModel

    @GET(Endpoints.REFRESH_TOKEN)
    suspend fun refreshToken(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        )
    ): RefreshToken

    @GET(Endpoints.GET_DIAMOND_CATEGORIES)
    suspend fun getDiamondCategories(
        @HeaderMap headers: Map<String, String> = mapOf(
            "X-PartnerCode" to LynkiD_SDK.partnerCode,
            "Authorization" to "Bearer ${LynkiD_SDK.accessToken}"
        ), @QueryMap queries: MutableMap<String, Any>
    ): GetDiamondCategoryResponseModel

}

object Endpoints {
    const val GENERATE_TOKEN = "api/sdk-v1/partner/generate-token"
    const val CHECK_MEMBER = "api/sdk-v1/check-member-and-connection"
    const val AUTH_CONNECTED_MEMBER = "api/sdk-v1/authen-with-connected-phone"
    const val CREATE_MEMBER = "api/sdk-v1/create-member"
    const val GET_MEMBER_INFO = "api/sdk-v1/Member/View"
    const val GET_POINT_INFO = "api/sdk-v1/Member/View-point"
    const val GET_BANNER_AND_NEWS = "api/sdk-v1/get-all-article-and-related-news"
    const val GET_HOME_CATEGORIES = "api/sdk-v1/get-list-categories"
    const val GET_HOME_GIFT_GROUP = "api/sdk-v1/get-gift-group"
    const val GET_GIFT_CATEGORIES = "api/GiftCategory/GiftListCategories_v1"
    const val GET_ALL_GIFT_GROUPS = "api/sdk-v1/get-gift-all-infors"
    const val GET_GIFTS_BY_CATEGORY = "api/sdk-v1/get-all-by-member-code"
    const val GET_MY_REWARDS = "api/sdk-v1/GetAllWithEGift"
    const val GET_GIFT_DETAILS = "api/sdk-v1/get-gift-details"
    const val CREATE_TRANSACTION = "api/sdk-v1/create-transaction"
    const val CONFIRM_TRANSACTION = "api/sdk-v1/confirm-otp-create-transaction"
    const val GET_TRANSACTIONS = "api/sdk-v1/Member/TokenTrans/GetByMemberId"
    const val GET_TRANSACTION_DETAIL = "api/sdk-v1/get-tx-detail"
    const val GET_LOCATIONS = "api/sdk-v1/Location/GetAll"
    const val GET_GIFT_USAGE_ADDRESS = "api/sdk-v1/GetGiftUsageAddress"
    const val GET_GIFTS = "api/sdk-v1/get-gift-all-infors"
    const val GET_MERCHANT = "api/sdk-v1/Merchant/GetAll"
    const val REFRESH_TOKEN = "api/sdk-v1/refresh-token"
    const val GET_DIAMOND_CATEGORIES = "api/sdk-v1/GetAllGiftCategoriesAndInfo"
}