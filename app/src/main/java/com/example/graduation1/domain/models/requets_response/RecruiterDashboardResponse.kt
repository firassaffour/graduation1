package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class RecruiterDashboardResponse(
    @SerializedName("jobID")            val jobID: Int,
    @SerializedName("jobTitle")         val jobTitle: String?,
    @SerializedName("totalCandidates")  val totalCandidates: Int,
    @SerializedName("candidates")       val candidates: List<CandidateRankingItem>
)