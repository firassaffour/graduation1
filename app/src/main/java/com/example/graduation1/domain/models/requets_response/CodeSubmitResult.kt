package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class CodeSubmitResult(
    @SerializedName("submission") val submission: CodeSubmissionResponse,
    @SerializedName("AIFeedback") val aiFeedback: AiResponseItem
)