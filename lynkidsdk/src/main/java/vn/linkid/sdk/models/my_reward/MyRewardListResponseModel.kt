package vn.linkid.sdk.models.my_reward

import vn.linkid.sdk.models.BaseModel

data class MyRewardListResponseModel(
    val message: String?,
    val isSuccess: Boolean?,
    val result: MyRewardList?
): BaseModel()
