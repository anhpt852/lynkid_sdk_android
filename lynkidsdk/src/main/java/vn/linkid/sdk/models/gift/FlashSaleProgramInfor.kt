package vn.linkid.sdk.models.gift
import FlashSaleListGiftItem
import com.google.gson.annotations.SerializedName
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class FlashSaleProgramInfor(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("code")
    val code: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("startTime")
    val startTime: String?,

    @SerializedName("endTime")
    val endTime: String?,

    @SerializedName("creationTime")
    val creationTime: String?,

    @SerializedName("creatorUserId")
    val creatorUserId: Int?,

    @SerializedName("creatorUserName")
    val creatorUserName: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("maxGiftPerCustomer")
    val maxGiftPerCustomer: Int?,

    @SerializedName("displayTime")
    val displayTime: String?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("image")
    val image: String?,

    @SerializedName("giftInFlashSaleProgramForViews")
    val gifts: List<FlashSaleListGiftItem>?
)

