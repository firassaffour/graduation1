package com.example.graduation1.domain.models.requets_response

import com.google.gson.annotations.SerializedName

data class MediaResponse(
    @SerializedName("mediaID")     val mediaID: Int,
    @SerializedName("filePath")    val filePath: String?,
    @SerializedName("fileType")    val fileType: String?,
    @SerializedName("uploadedAt")  val uploadedAt: String?,
    @SerializedName("postID")      val postID: Int?
)