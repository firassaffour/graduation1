package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class JobResponse(
    @SerializedName("jobID")              val jobID: Int,
    @SerializedName("title")              val title: String,
    @SerializedName("description")        val description: String?,
    @SerializedName("location")           val location: String?,
    @SerializedName("experienceLevel")    val experienceLevel: String?,
    @SerializedName("requiredSkillsJson") val requiredSkillsJson: String?,
    @SerializedName("postedByUserID")     val postedByUserID: Int,
    @SerializedName("isOpen")             val isOpen: Boolean,
    @SerializedName("createdAt")          val createdAt: String
)