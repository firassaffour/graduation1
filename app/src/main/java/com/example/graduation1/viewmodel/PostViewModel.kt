package com.example.graduation1.viewmodel

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.data.repository.MediaRepository
import com.example.graduation1.data.repository.PostRepository
import com.example.graduation1.data.repository.UserRepository
import com.example.graduation1.domain.models.Comment
import com.example.graduation1.domain.models.requets_response.MediaResponse
import com.example.graduation1.domain.models.PostData
import com.example.graduation1.domain.models.requets_response.PostResponse
import com.example.graduation1.domain.models.requets_response.RegisterResponse
import com.example.graduation1.domain.models.User
import com.example.graduation1.domain.models.requets_response.CreateCommentRequest
import com.example.graduation1.domain.models.requets_response.CreatePostRequest
import com.example.graduation1.language
import com.example.graduation1.savedPost
import com.example.graduation1.user
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.SecureRandom
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@RequiresApi(Build.VERSION_CODES.O)
class PostViewModel(private val postRepository: PostRepository, private val userRepository: UserRepository, private val mediaRepository: MediaRepository): ViewModel() {

    private val _currentUser = MutableStateFlow<User>(user)
    val currentUser  = _currentUser.asStateFlow()

    private val _posts = MutableStateFlow<List<PostData>>(emptyList())
    val posts = _posts.asStateFlow()

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments = _comments.asStateFlow()

    private val _favouritePosts = MutableStateFlow<List<PostData>>(emptyList())
    val favouritePosts = _favouritePosts.asStateFlow()

    private val _savedPosts = MutableStateFlow<List<PostData>>(emptyList())
    val savedPosts = _savedPosts.asStateFlow()

    private val _postText = MutableStateFlow("")
    val postText  = _postText.asStateFlow()

    private val _postCodeSnippet = MutableStateFlow("")
    val postCodeSnippet  = _postCodeSnippet.asStateFlow()

    private val _commentText = MutableStateFlow("")
    val commentText  = _commentText.asStateFlow()

    private val _newCommentId = MutableStateFlow("")
    val newCommentId  = _newCommentId.asStateFlow()

    private val _newPostId = MutableStateFlow("")
    val newPostId  = _newPostId.asStateFlow()

    private val _isPostsLoading = MutableStateFlow<Boolean>(true)
    val isPostsLoading = _isPostsLoading.asStateFlow()

    private val _isCommentsLoading = MutableStateFlow<Boolean>(true)
    val isCommentsLoading = _isCommentsLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val BASE_URL = "https://graduation-project-backend-production-bc68.up.railway.app"

    init {
        getSavedPosts()
        getPosts()
        Log.d("VM", "postViewModel created ${this.hashCode()} ")
    }

    private fun getCurrentUser(){
        viewModelScope.launch {
            try {
                _currentUser.value = userRepository.getCurrentUser()
                Log.d("userViewModel", "loadUsers: ${_currentUser.value}")
            }
            catch (e: Exception){
                Log.e("userViewModel", "loadUsers: ${e.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPosts(){
        viewModelScope.launch {
            try {
                _posts.value = postRepository.getPosts().map {
                    val media = mediaRepository.getMediaByPost(it.postId.toInt())
                    val image = media.find { !it.filePath.isNullOrEmpty() }?.filePath ?: ""
                    it.copy(isLiked = postRepository.getLikeStatus(it.postId.toInt()),
                        isSaved = _savedPosts.value.contains(it),
                        postImage = if (!image.isNullOrEmpty()) "$BASE_URL$image" else "")
                }

                Log.d("API", "postViewModel getting posts success: ${_posts.value}")
            }
            catch (e: Exception){
                Log.e("API", "postViewModel getting posts error: ${e.message}")
            }
            finally {
                _isPostsLoading.value = false
            }
        }
    }

    fun getPostsCount(userId : String) : Int{
        val postsCount = mutableIntStateOf(0)
        viewModelScope.launch {
            try {
                _posts.value.map {
                    if (it.userId == userId)
                        postsCount.intValue += 1
                }
            }
            catch (e: Exception){
                Log.e("API", "postViewMode get post count: ${e.message}")
            }
        }
        return postsCount.intValue
    }

    fun getFavouritePosts(){
        viewModelScope.launch {
            try {
                _favouritePosts.value = _posts.value.filter { it.isLiked }
            }
            catch (e: Exception){
                Log.e("API", "postViewModel get favourite post: ${e.message}")
            }
        }
    }

    fun getSavedPosts(){
        viewModelScope.launch {
            try {
                _savedPosts.value = postRepository.getSavedPosts()
            }
            catch (e: Exception){
                Log.e("API", "postViewModel get saved post: ${e.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createPost(groupId: String, imageUri: Uri?) {
        viewModelScope.launch {
            try {

                // 2. Create the post on the backend
                val postData = CreatePostRequest(
                    title       = null,
                    content     = _postText.value,
                    codeSnippet = _postCodeSnippet.value.ifBlank { null },
                    communityID = groupId.toIntOrNull()
                )
                val response = postRepository.createPost(postData)

                // 1. Upload image if the user picked one
                var uploadedImageUrl: String? = null
                if (imageUri != null) {
                    val media = mediaRepository.uploadImage(imageUri, postId = response.postID)
                    uploadedImageUrl = media.filePath
                    Log.d("PostVM", "Image uploaded: $uploadedImageUrl")
                }

                // 3. Optimistically add to UI list
                val newPost = PostData(
                    postId      = response.postID.toString(),
                    groupId     = groupId,
                    userId      = _currentUser.value.id,
                    postText    = _postText.value,
                    postImage   = uploadedImageUrl ?: "",
                    codeSnippet = _postCodeSnippet.value,
                    createdAt   = System.currentTimeMillis(),
                    likesCount  = 0,
                    isLiked     = false
                )
                _posts.value = listOf(newPost) + _posts.value
                _newPostId.value = newPost.postId

                _postText.value = ""
                _postCodeSnippet.value = ""
                Log.d("PostVM", "createPost OK: ${response.postID}")
            } catch (e: Exception) {
                _error.value = "Failed to create post: ${e.message}"
                Log.e("PostVM", "createPost: ${e.message}")
            }
        }
    }


    fun removePost(postId: String) {
        viewModelScope.launch {
            try {
                val post = _posts.value.find { it.postId == postId } ?: return@launch
                _posts.value = _posts.value - post

                postRepository.deletePost(postId)
                Log.e("API", "postViewModel delete post success")
            }
            catch (e: Exception){
                Log.e("API", "postViewModel delete post error: ${e.message}")
            }
        }
    }

    fun getTimeAgo(createdAt : Long) : String{
        val diff = System.currentTimeMillis() - createdAt

        val seconds = diff / 1000
        val minutes = diff / (1000 * 60)
        val hours = diff / (1000 * 60 * 60)
        val days = diff / (1000 * 60 * 60 * 24)

        val secondsText = if (language == "en") "now" else "الأن"
        val minutesText = if (language == "en") "$minutes minutes" else "$minutes دقيقة "
        val hoursText = if (language == "en") "$hours hours" else "$hours ساعة "
        val daysText = if (language == "en") "$days days" else "$days يوم "

        return when{
            seconds < 60 -> secondsText
            minutes < 60 -> minutesText
            hours < 24 -> hoursText
            days < 7 -> daysText
            else -> daysText
        }
    }

    fun updatePostText(postText: String){
        _postText.value = postText
    }

    fun updatePostCodeSnippet(postCodeSnippet: String){
        _postCodeSnippet.value = postCodeSnippet
    }

    fun updateCommentText(commentText: String){
        _commentText.value = commentText
    }

    fun updateNewPostId(postId: String){
        _newPostId.value = postId
    }

    fun updateNewCommentId(commentId: String){
        _newCommentId.value = commentId
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalUuidApi::class)
    fun createComment(postId: String){
        viewModelScope.launch {
            try {
                val comment = Comment(
                    SecureRandom().nextInt(Int.MAX_VALUE).toString(),
                    _currentUser.value.id,
                    _commentText.value,
                    System.currentTimeMillis()
                )

                _posts.value = _posts.value.map {
                    if (it.postId == postId)
                        it.copy(commentsList = listOf(comment) + it.commentsList)

                    else it
                }

                val commentData = CreateCommentRequest(
                    postId.toInt(),
                    _commentText.value
                )
                val response = postRepository.createComment(commentData)

                _newCommentId.value = comment.commentId
                _commentText.value = ""

                Log.d("API", "createComment success: $response")
            }
            catch (e: Exception){
                Log.e("API", "postViewModel create comment error: ${e.message}")
            }
        }
    }

    fun removeComment(postId: String, commentId: String) {
        val post = _posts.value.find { it.postId == postId } ?: return
        val comment = post.commentsList.find { it.commentId == commentId } ?: return
        _posts.value = _posts.value.map {
            if (it.postId == postId){
                it.copy(commentsList = it.commentsList - comment)
            }

            else it
        }

        viewModelScope.launch {
            try {
                postRepository.deleteComment(commentId.toInt())
                Log.d("API", "removeComment SUCCESS: ")
            }
            catch (e : Exception){
                Log.e("API", "removeComment failed: ${e.message}", )
            }
        }
    }

    fun getCommentsByPost(postId: String) {
        viewModelScope.launch {
            try {
                _comments.value = postRepository.getCommentsByPost(postId.toInt())
                Log.d("API", "getCommentsByPost success: ")
            } catch (e: Exception) {
                Log.e("API", "getCommentsByPost error: ${e.message}",)
            }
            finally {
                _isCommentsLoading.value = false
            }
        }
    }

    fun toggleLike(postId: String){
        _posts.value = _posts.value.map {
            if (it.postId == postId)
                if (!it.isLiked)
                    it.copy(isLiked = true, likesCount = it.likesCount + 1 )
                else
                    it.copy(isLiked = false, likesCount = it.likesCount - 1)
            else it
        }
        viewModelScope.launch {
            try {
                val response = postRepository.toggleLike(postId.toInt())
                Log.d("API", "toggleLike success: $response")
            } catch (e: Exception) {
                Log.e("API", "toggleLike error: ${e.message}",)
            }
        }
    }

    fun getLikeStatus(postId: String) : Boolean{
        val response = false
        viewModelScope.launch {
            try {
                postRepository.getLikeStatus(postId.toInt())
                Log.d("API", "getLikesStatus success:")
            }
            catch (e : Exception){
                Log.d("API", "getLikesStatus error: ${e.message}")
            }
        }
        return response
    }

    fun getLikesCount(postId: String) : Int{
        var likeCount = 0
        viewModelScope.launch {
            try {
                likeCount = postRepository.getLikeCount(postId.toInt())
                Log.d("API", "getLikesCount success: $likeCount")
            }
            catch (e : Exception){
                Log.d("API", "getLikesCount error: ${e.message}")
            }
        }
        return likeCount
    }

    fun toggleSaved(post : PostData){
        _posts.value = _posts.value.map {
            if (it.postId == post.postId)
                it.copy(isSaved = !it.isSaved)

            else it
        }

        viewModelScope.launch {
            try {
                if (post.isSaved) postRepository.savePost(post.postId.toInt())
                else postRepository.unsavePost(post.postId.toInt())

                Log.d("API", "toggleSaved success: ")
            }
            catch (e : Exception){
                Log.e("API", "toggleSaved error : ${e.message}", )
            }
        }
    }

    fun toggleLikeComment(commentId: String, postId: String){
        _posts.value = _posts.value.map { post ->
            if (post.postId == postId) {
                val updatedComment = post.commentsList.map {comment ->
                    if (comment.commentId == commentId) {
                        if (!comment.isLiked)
                            comment.copy(likesCount = comment.likesCount + _currentUser.value.id, isLiked = true)
                        else
                            comment.copy(likesCount = comment.likesCount - _currentUser.value.id, isLiked = false)
                    }
                    else comment
                }
                post.copy(commentsList = updatedComment)
            }

            else post
        }

        _comments.value = _comments.value.map {comment ->
                if (comment.commentId == commentId) {
                    if (!comment.isLiked)
                        comment.copy(likesCount = comment.likesCount + _currentUser.value.id, isLiked = true)
                    else
                        comment.copy(likesCount = comment.likesCount - _currentUser.value.id, isLiked = false)
                }
                else comment
        }
    }

    fun refreshData(){
        getPosts()
    }
}