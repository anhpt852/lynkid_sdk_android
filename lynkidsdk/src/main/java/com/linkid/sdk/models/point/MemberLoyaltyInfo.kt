package com.linkid.sdk.models.point

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