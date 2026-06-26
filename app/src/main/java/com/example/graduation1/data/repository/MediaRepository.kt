package com.example.graduation1.data.repository

import android.content.Context
import android.net.Uri
import com.example.graduation1.data.remote.ApiService
import com.example.graduation1.domain.models.requets_response.MediaResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Path

class MediaRepository(private val api: ApiService, private val context: Context) {

    suspend fun uploadImage(
        uri: Uri,
        postId: Int? = null,
        messageId: Int? = null,
        commentId: Int? = null
    ): MediaResponse {
        val stream = context.contentResolver.openInputStream(uri)
            ?: error("Cannot open image stream for $uri")

        val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
        val extension = when (mimeType) {
            "image/png"  -> "png"
            "image/webp" -> "webp"
            "image/gif"  -> "gif"
            else         -> "jpg"
        }

        val bytes = stream.use { it.readBytes() }
        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("file", "upload.$extension", requestBody)

        return api.uploadMedia(part, postId, messageId, commentId)
    }

    suspend fun getMediaByPost(postId: Int): List<MediaResponse> {return api.getMediaByPost(postId)}
}