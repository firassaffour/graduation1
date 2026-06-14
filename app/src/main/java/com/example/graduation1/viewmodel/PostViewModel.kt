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
import com.example.graduation1.domain.models.PostData
import com.example.graduation1.favouritePost
import com.example.graduation1.language
import com.example.graduation1.postList
import com.example.graduation1.savedPost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class PostViewModel(private val postRepository: PostRepository, private val userRepository: UserRepository): ViewModel() {

    private val _currentUser = userRepository.currentUser
    val currentUser  = _currentUser

    private val _posts = MutableStateFlow<List<PostData>>(emptyList())
    val posts = _posts.asStateFlow()

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

    private fun getPosts(){
        viewModelScope.launch {
            try {
                _posts.value = postList
            }
            catch (e: Exception){
                Log.e("API", "postViewModel get post: ${e.message}")
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
                _favouritePosts.value = favouritePost
            }
            catch (e: Exception){
                Log.e("API", "postViewModel get favourite post: ${e.message}")
            }
        }
    }

    fun getSavedPosts(){
        viewModelScope.launch {
            try {
                _savedPosts.value = savedPost
            }
            catch (e: Exception){
                Log.e("API", "postViewModel get saved post: ${e.message}")
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    fun createPost(groupId : String, groupName : String, groupImage : String , postImage : String?){
        viewModelScope.launch {
            try {
                val post = PostData(
                    Uuid.random().toString(),
                    groupId,
                    currentUser.id,
                    _postText.value,
                    postImage!!,
                    _postCodeSnippet.value,
                    System.currentTimeMillis(),
                    emptyList(),
                    false,
                    emptyList(),

                )
                _posts.value = listOf(post) + _posts.value
                _newPostId.value = post.postId

                _postText.value = ""
                _postCodeSnippet.value = ""

            }
            catch (e: Exception){
                Log.e("API", "postViewModel create post: ${e.message}")
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
                    Uuid.random().toString(),
                    _currentUser.id,
                    _commentText.value,
                    System.currentTimeMillis()
                )

                _posts.value = _posts.value.map {
                    if (it.postId == postId)
                        it.copy(commentsList = listOf(comment) + it.commentsList)

                    else it
                }
                _newCommentId.value = comment.commentId
                _commentText.value = ""
            }
            catch (e: Exception){
                Log.e("API", "postViewModel create comment: ${e.message}")
            }
        }
    }

    fun toggleLike(postId: String){
        _posts.value = _posts.value.map {
            if (it.postId == postId)
                if (!it.likesCount.contains(_currentUser.id))
                    it.copy(likesCount = it.likesCount + _currentUser.id)
                else
                    it.copy(likesCount = it.likesCount - _currentUser.id)
            else it
        }
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
                        if (!comment.likesCount.contains(_currentUser.id))
                            comment.copy(likesCount = comment.likesCount + _currentUser.id)
                        else
                            comment.copy(likesCount = comment.likesCount - _currentUser.id)
                    }
                    else comment
                }
                post.copy(commentsList = updatedComment)
            }

            else post
        }
    }
}