package com.linkid.sdk.home.models

import com.linkid.sdk.BaseModel

data class MemberResponseModel(
    val result: Int?,
    val items: Member?
) : BaseModel()

data class Member(
    val id: Int?,
    val userAddress: String?,
    val status: String?,
    val type: String?,
    val name: String?,
    val address: String?,
    val phoneNumber: String?,
    val dob: String?,
    val nationalId: String?,
    val idCard: String?,
    val partnerPhoneNumber: String?,
    val pointUsingOrdinary: String?,
    val gender: String?,
    val email: String?,
    val hashAddress: String?,
    val regionCode: String?,
    val fullRegionCode: String?,
    val memberTypeCode: String?,
    val fullMemberTypeCode: String?,
    val channelType: String?,
    val fullChannelTypeCode: String?,
    val rankTypeCode: String?,
    val standardMemberCode: String?,
    val tempPointBalance: Int?,
    val tokenBalance: Int?,
    val referralCode: String?,
    val avatar: String?,
    val grantTypeBalance: List<GrantTypeBalance>?,
    val isIdCardVerified: Boolean?,
    val hasPinCode: Boolean?,
    val nationId: Int?,
    val cityId: Int?,
    val districtId: Int?,
    val wardId: Int?,
    val streetDetail: String?
)

data class GrantTypeBalance(
    val grantType: String?,
    val balance: Int?
)