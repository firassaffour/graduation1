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
import com.example.graduation1.domain.models.Comment
import com.example.graduation1.domain.models.PostData
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

class PostViewModel(private val repository: PostRepository): ViewModel() {

    val currentUser = user

    private val _posts = MutableStateFlow<List<PostData>>(emptyList())
    val posts = _posts.asStateFlow()

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments = _comments.asStateFlow()

    private val _favouritePosts = MutableStateFlow<List<PostData>>(emptyList())
    val favouritePosts = _favouritePosts.asStateFlow()

    private val _savedPosts = MutableStateFlow<List<PostData>>(emptyList())
    val savedPosts = _savedPosts.asStateFlow()

    init {
        _posts.value = postList
        getPosts()
    }

    private fun getPosts(){
        viewModelScope.launch {
            try {
                //posts = repository.getPosts()
            }
            catch (e: Exception){
                Log.e("API", "postViewModel: ${e.message}")
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
                Log.e("API", "postViewModel: ${e.message}")
            }
        }
        return postsCount.intValue
    }

    fun getComments(postId: String){
        viewModelScope.launch {
            try {
                _posts.value.map {
                    if (it.postId == postId)
                        _comments.value = it.commentsList
                }
                //_comments = repository.getComments()
            }
            catch (e: Exception){
                Log.e("API", "postViewModel: ${e.message}")
            }
        }
    }

    fun getFavouritePosts(){
        viewModelScope.launch {
            try {
                _favouritePosts.value = favouritePost
            }
            catch (e: Exception){
                Log.e("API", "postViewModel: ${e.message}")
            }
        }
    }

    fun getSavedPosts(){
        viewModelScope.launch {
            try {
                _savedPosts.value = savedPost
            }
            catch (e: Exception){
                Log.e("API", "postViewModel: ${e.message}")
            }
        }
    }

    fun createPost(post: PostData){
        viewModelScope.launch {
            try {
                _posts.value += post
                //repository.createPost(post)
            }
            catch (e: Exception){
                Log.e("API", "postViewModel: ${e.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalUuidApi::class)
    fun createComment(commentText: String, postId: String){
        viewModelScope.launch {
            try {
                val time = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("hh:mm")
                val formatted = time.format(formatter)
                val comment = Comment(
                    Uuid.random().toString(),
                    currentUser.name,
                    currentUser.image,
                    commentText,
                    formatted
                )
                _comments.value = listOf(comment) + _comments.value

                _posts.value = _posts.value.map {
                    if (it.postId == postId)
                        it.copy(commentsCount = it.commentsCount + 1)

                    else it
                }
            }
            catch (e: Exception){
                Log.e("API", "postViewModel: ${e.message}")
            }
        }
    }

fun updateLike(postId : String, post: PostData){
    viewModelScope.launch {
        try {
            repository.updateLike(postId, post)
        }
        catch (e: Exception){
            Log.e("API", "postViewModel: ${e.message}")
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

    fun toggleLikeComment(commentId: String){
        _comments.value = _comments.value.map {
            if (it.id == commentId) {
                if (!it.isLiked)
                    it.copy(likesCount = it.likesCount + 1, isLiked = !it.isLiked)
                else
                    it.copy(likesCount = it.likesCount - 1, isLiked = !it.isLiked)
            }

            else it
        }
    }
}