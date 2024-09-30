package vn.linkid.sdk.models.imedia

import vn.linkid.sdk.models.BaseModel

data class GetIMediaHistoryResponseModel(
    val result: GetIMediaHistoryResult?
) : BaseModel()

data class GetIMediaHistoryResult(
    val totalCount: Int?,
    val items: List<GetIMediaHistory>?
)
