package com.linkid.sdk
import com.linkid.sdk.home.models.HomeNewsAndBannerModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap

val client = OkHttpClient()
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://vpid-mobile-api-uat.linkid.vn")
//    .baseUrl("https://vpid-mobile-api.linkid.vn")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
val mainAPI: APIEndpoints = retrofit.create(APIEndpoints::class.java)

interface APIEndpoints {
    @GET("api/Article/GetAllArticleAndRelatedNews_Optimize")
    suspend fun getBannerAndNews(@QueryMap queries: Map<String, Any?>): HomeNewsAndBannerModel
}