package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class JobRequest(
    @SerializedName("title")           val title: String,
    @SerializedName("description")     val description: String? = null,
    @SerializedName("location")        val location: String? = null,
    @SerializedName("experienceLevel") val experienceLevel: String? = null,
    @SerializedName("requiredSkills")  val requiredSkills: List<String>? = null
)