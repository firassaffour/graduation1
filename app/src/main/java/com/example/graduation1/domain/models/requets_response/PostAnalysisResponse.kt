package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class PostAnalysisResponse(
    @SerializedName("score")       val score: Int,
    @SerializedName("isCorrect")   val isCorrect: Boolean,
    @SerializedName("confidence")  val confidence: Int,
    @SerializedName("correction")  val correction: String?,
    @SerializedName("reason")      val reason: String?,
    @SerializedName("difficulty")  val difficulty: String?,
    @SerializedName("tags")        val tags: List<String>
)