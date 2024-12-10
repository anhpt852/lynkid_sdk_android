package vn.linkid.sdk.models.transaction

import vn.linkid.sdk.models.my_reward.VendorInfo

data class GroupTransactionItem(
    val date: String,
    val transactions: MutableList<GetTransactionItem>
)