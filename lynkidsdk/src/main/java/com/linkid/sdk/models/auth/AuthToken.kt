package com.linkid.sdk.models.auth

data class AuthToken(
    val seedToken: String,
    val isSuccess: Boolean,
    val code: String,
    val message: String
)