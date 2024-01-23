package com.linkid.sdk.home.models

import com.linkid.sdk.BaseModel

data class PointResponseModel(
    val result: Int?,
    val items: Point?
) : BaseModel()

data class Point(
    val id: Int?,
    val memberName: String?,
    val avatar: String?,
    val totalEquivalentAmount: Int?,
    val tokenBalance: Int?,
    val expiringTokenAmount: Int?,
    val expiringDate: String?,
    val tempPointBalance: Int?,
    val partnerPointBalance: List<PartnerPointBalance>?,
    val totalCount: Int?,
    val pointUsageType: String?,
    val grantTypeBalance: List<GrantTypeBalance>?,
    val memberLoyaltyInfo: MemberLoyaltyInfo?
)

data class PartnerPointBalance(
    val merchantId: Int?,
    val merchantName: String?,
    val pointBalance: Int?
)

data class MemberLoyaltyInfo(
    val memberCode: String?,
    val point: Int?,
    val coin: Int?,
    val rankId: Int?,
    val rank: Rank?,
    val nextRankId: Int?,
    val nextRank: Rank?,
    val tempRankId: Int?,
    val tempRank: Rank?,
    val expiringCoin: Int?,
    val pointToNextTempRank: Int?,
    val pointToKeepRank: Int?
)

data class Rank(
    val id: Int?,
    val code: String?,
    val name: String?,
    val target: Int?,
    val description: String?,
    val avatar: String?,
    val isActive: Boolean?,
    val articleId: Int?
)