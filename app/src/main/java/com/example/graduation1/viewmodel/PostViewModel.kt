package com.example.graduation1.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.commentsList
import com.example.graduation1.data.repository.PostRepository
import com.example.graduation1.data.repository.UserRepository
import com.example.graduation1.domain.models.Comment
import com.example.graduation1.domain.models.PostData
import com.example.graduation1.domain.models.User
import com.example.graduation1.favouritePost
import com.example.graduation1.postList
import com.example.graduation1.savedPost
import com.example.graduation1.user
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
                val time = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("hh:mm")
                val formatted = time.format(formatter)
                val post = PostData(
                    Uuid.random().toString(),
                    groupId,
                    currentUser.id,
                    groupName,
                    groupImage,
                    currentUser.name,
                    currentUser.image.toString(),
                    _postText.value,
                    postImage!!,
                    _postCodeSnippet.value,
                    formatted,
                    false,
                    false,
                    0,
                    emptyList()

                )
                _posts.value += post

                _postText.value = ""
                _postCodeSnippet.value = ""

            }
            catch (e: Exception){
                Log.e("API", "postViewModel create post: ${e.message}")
            }
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

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalUuidApi::class)
    fun createComment(postId: String){
        viewModelScope.launch {
            try {
                val time = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("hh:mm")
                val formatted = time.format(formatter)
                val comment = Comment(
                    Uuid.random().toString(),
                    _currentUser.name,
                    _currentUser.image,
                    _commentText.value,
                    formatted
                )

                _posts.value = _posts.value.map {
                    if (it.postId == postId)
                        it.copy(commentsList = listOf(comment) + it.commentsList, commentsCount = it.commentsCount + 1)

                    else it
                }
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
                if (!it.isLiked)
                    it.copy(likesCount = it.likesCount + 1, isLiked = !it.isLiked)
                else
                    it.copy(likesCount = it.likesCount - 1, isLiked = !it.isLiked)
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
                    if (comment.id == commentId) {
                        if (!comment.isLiked)
                            comment.copy(likesCount = comment.likesCount + 1, isLiked = !comment.isLiked)
                        else
                            comment.copy(likesCount = comment.likesCount - 1, isLiked = !comment.isLiked)
                    }
                    else comment
                }
                post.copy(commentsList = updatedComment)
            }

            else post
        }
    }
}