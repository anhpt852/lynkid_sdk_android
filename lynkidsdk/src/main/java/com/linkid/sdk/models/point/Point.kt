package com.linkid.sdk.models.point

import com.linkid.sdk.models.member.GrantTypeBalance


data class Point(
    val id: Int?,
    val memberName: String?,
    val avatar: String?,
    val totalEquivalentAmount: Double?,
    val tokenBalance: Double?,
    val expiringTokenAmount: Double?,
    val expiringDate: String?,
    val tempPointBalance: Double?,
    val partnerPointBalance: List<PartnerPointBalance>?,
    val totalCount: Double?,
    val pointUsageType: String?,
    val grantTypeBalance: List<GrantTypeBalance>?,
    val memberLoyaltyInfo: MemberLoyaltyInfo?
)





