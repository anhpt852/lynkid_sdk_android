package vn.linkid.sdk

open class BaseModel(
    var targetUrl: String? = null,
    var success: Boolean? = null,
    var error: String? = null,
    var unAuthorizedRequest: Boolean? = null,
    var messageDetail: String? = null,
    var details: String? = null
)