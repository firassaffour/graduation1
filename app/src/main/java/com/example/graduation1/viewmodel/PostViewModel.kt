package com.example.graduation1.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class PostViewModel(private val postRepository: PostRepository, private val userRepository: UserRepository): ViewModel() {

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

    init {
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
                    it.copy(commentsList = _comments.value)
                }

                Log.d("API", "postViewModel getting posts: ${_posts.value}")
            }
            catch (e: Exception){
                Log.e("API", "postViewModel getting posts: ${e.message}")
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
                _favouritePosts.value = _posts.value.filter { it.likesCount.contains(_currentUser.value.id) }
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

    @OptIn(ExperimentalUuidApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    fun createPost(groupId : String, postImage : String?){
        viewModelScope.launch {
            try {
                val post = PostData(
                    SecureRandom().nextInt(Int.MAX_VALUE).toString(),
                    groupId,
                    currentUser.value.id,
                    _postText.value,
                    postImage!!,
                    _postCodeSnippet.value,
                    System.currentTimeMillis(),
                    emptyList(),
                    false,
                    emptyList(),

                )

                val current = RegisterResponse(
                    _currentUser.value.id.toInt(),
                    _currentUser.value.name,
                    _currentUser.value.name,
                    _currentUser.value.email,
                    _currentUser.value.description,
                    _currentUser.value.image.toString(),
                )

                val postData = CreatePostRequest(
                    title = "",
                    content = _postText.value,
                    codeSnippet = _postCodeSnippet.value,
                    communityID = groupId.toInt(),
                )
                _posts.value = listOf(post) + _posts.value
                _newPostId.value = post.postId

                val response = postRepository.createPost(postData)

                _postText.value = ""
                _postCodeSnippet.value = ""

                Log.d("API", "postViewModel create post success: ${response}")

            }
            catch (e: Exception){
                Log.e("API", "postViewModel create post error: ${e.message} $groupId")
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
        }
    }

    fun toggleLike(postId: String){
        _posts.value = _posts.value.map {
            if (it.postId == postId)
                if (!it.likesCount.contains(_currentUser.value.id))
                    it.copy(likesCount = it.likesCount + _currentUser.value.id)
                else
                    it.copy(likesCount = it.likesCount - _currentUser.value.id)
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
        var isLiked = false
        viewModelScope.launch {
            try {
                isLiked =  postRepository.getLikeStatus(postId.toInt())
                Log.d("API", "getLikesStatus success: $isLiked")
            }
            catch (e : Exception){
                Log.d("API", "getLikesStatus error: ${e.message}")
            }
        }
        return isLiked
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

    fun toggleSaved(postId: String){
        _posts.value = _posts.value.map {
            if (it.postId == postId)
                it.copy(isSaved = !it.isSaved)

            else it
        }
    }

    fun toggleLikeComment(commentId: String, postId: String){
        _posts.value = _posts.value.map { post ->
            if (post.postId == postId) {
                val updatedComment = post.commentsList.map {comment ->
                    if (comment.commentId == commentId) {
                        if (!comment.likesCount.contains(_currentUser.value.id))
                            comment.copy(likesCount = comment.likesCount + _currentUser.value.id)
                        else
                            comment.copy(likesCount = comment.likesCount - _currentUser.value.id)
                    }
                    else comment
                }
                post.copy(commentsList = updatedComment)
            }

            else post
        }
    }

    fun refreshData(){
        getPosts()
    }
}