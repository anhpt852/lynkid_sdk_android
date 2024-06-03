package vn.linkid.sdk.models.gift

data class GiftGroupInside(
    val code: String?,
    val name: String?
) {
    companion object {
        fun fromJson(json: Map<String, Any>): GiftGroupInside {
            return GiftGroupInside(
                code = json["code"] as String?,
                name = json["name"] as String?
            )
        }
    }

    fun toJson(): Map<String, Any?> {
        return mapOf(
            "code" to code,
            "name" to name
        )
    }
}
