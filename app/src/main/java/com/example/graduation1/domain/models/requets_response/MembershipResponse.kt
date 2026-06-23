package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class MembershipResponse(
    @SerializedName("isMember")    val isMember: Boolean,
    @SerializedName("communityID") val communityID: Int
)