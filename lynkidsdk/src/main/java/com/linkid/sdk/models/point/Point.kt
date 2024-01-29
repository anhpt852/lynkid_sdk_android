package com.linkid.sdk.models.point

import com.linkid.sdk.models.member.GrantTypeBalance


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





