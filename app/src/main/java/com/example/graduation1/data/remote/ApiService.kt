package com.example.graduation1.data.remote

import com.example.graduation1.domain.models.AuthResponse
import com.example.graduation1.domain.models.CandidateData
import com.example.graduation1.domain.models.ChatItem
import com.example.graduation1.domain.models.CodeSubmissionData
import com.example.graduation1.domain.models.Comment
import com.example.graduation1.domain.models.Group
import com.example.graduation1.domain.models.JobApplicationData
import com.example.graduation1.domain.models.JobsData
import com.example.graduation1.domain.models.LoginRequest
import com.example.graduation1.domain.models.Message
import com.example.graduation1.domain.models.Notification
import com.example.graduation1.domain.models.PostData
import com.example.graduation1.domain.models.PostResponse
import com.example.graduation1.domain.models.RecruiterData
import com.example.graduation1.domain.models.RegisterRequest
import com.example.graduation1.domain.models.RegisterResponse
import com.example.graduation1.domain.models.User
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    // Auth
    @POST("api/Auth/register")
    suspend fun createAccount(
        @Body registerRequest: RegisterRequest
    ) : RegisterResponse

    @POST("api/Auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ) : AuthResponse

    //Users
    @GET("api/users")
    suspend fun getUsers() : List<RegisterResponse>

    @GET("api/users/me")
    suspend fun getCurrentUser() : RegisterResponse

    @GET("api/users/{id}")
    suspend fun getUserDetails(
        @Path("id") id : String) : User

    @PUT("api/users/{id}")
    suspend fun editUser(
        @Path("id") id : String,
        @Body user : User) : User

    @DELETE("api/users/{id}")
    suspend fun deleteUser(
        @Path("id") id : String) : User

    //follow
    @POST("api/Follow/{userId}")
    suspend fun followUser(
        @Path("userId") id: String
    ) : Boolean

    @DELETE("api/Follow/{userId}")
    suspend fun unfollowUser(
        @Path("userId") id: String
    ) : Boolean

    @GET("api/Follow/{userId}/followers")
    suspend fun getFollowers(
        @Path("userId") id: String
    ) : List<User>

    @GET("api/Follow/{userId}/following")
    suspend fun getFollowing(
        @Path("userId") id: String
    ) : List<User>

    @GET("api/Follow/{userId}/status")
    suspend fun getFollowStatus(
        @Path("userId") id: String
    ) : Boolean


    //Posts
    @GET("api/posts")
    suspend fun getPosts() : List<PostResponse>

    @GET("api/posts/{id}")
    suspend fun getPostDetails(
        @Path("id") id: String) : PostResponse

    @POST("api/posts")
    suspend fun createPost(
        @Body post : PostResponse
    ) : PostResponse

    @PUT("api/posts/{id}")
    suspend fun updateLike(
        @Path("id") id : String,
        @Body post: PostData) : PostData

    @DELETE("api/posts/{id}")
    suspend fun deletePost(
        @Path("id") id: String) : PostData

    //comments
    @POST("api/Comments")
    suspend fun createComment(
        @Body comment: Comment
    ) : Comment

    @GET("api/Comments/post/{postId}")
    suspend fun getComments(
        @Path("id") id: String
    ) : List<Comment>

    @GET("api/Comments/{id}")
    suspend fun getCommentDetails(
        @Path("id") id: String
    ) : Comment

    @PUT("api/Comments/{id}")
    suspend fun editComment(
        @Path("id") id: String,
        @Body comment: Comment
    ) : Comment

    @DELETE("api/Comments/{id}")
    suspend fun deleteComment(
        @Path("id") id: String
    ) : Comment

    //Like
    @POST("api/Likes/toggle/{postId}")
    suspend fun toggleLikePost(
        @Path("postId") id: String
    ) : PostData

    @GET("api/Likes/count/{postId}")
    suspend fun getLikesCountPost(
        @Path("postId") id: String
    ) : Int

    @GET("api/Likes/status/{postId}")
    suspend fun getIsLikedPost(
        @Path("postId") id: String
    ) : Boolean

    // savedPosts
    @GET("api/SavedPosts")
    suspend fun getSavedPosts() : List<PostData>

    @POST("api/SavedPosts/{postId}")
    suspend fun addSavedPost(
        @Path("postId") id: String
    ) : PostData

    @DELETE("api/SavedPosts/{postId}")
    suspend fun deleteSavedPost(
        @Path("postId") id: String
    ) : PostData

    //Communities
    @GET("api/Communities")
    suspend fun getCommunities() : List<Group>

    @POST("api/Communities")
    suspend fun createCommunity(
        @Body group: Group
    ) : Group

    @POST("api/Communities/{communityId}/join")
    suspend fun joinCommunity(
        @Path("communityId") id: String
    ) : Group

    @DELETE("api/Communities/{communityId}/leave")
    suspend fun leaveCommunity(
        @Path("communityId") id: String
    ) : Group

    @GET("api/Communities/{communityId}/members")
    suspend fun getCommunityMembers(
        @Path("communityId") id: String
    ) : List<User>

    @GET("api/Communities/{communityId}/membership")
    suspend fun getCommunityMembersShip(
        @Path("communityId") id: String
    ) : List<User>

    //Messages
    @GET("api/Messages")
    suspend fun getAllChat() : List<ChatItem>

    @GET("api/Messages/conversation/{otherUserId}")
    suspend fun getChatContent(
        @Path("otherUserId") id: String
    ) : List<Message>

    @POST("api/Messages")
    suspend fun sendMessage(
        @Body message: Message
    ) : Message

    @DELETE("api/Messages/{id}")
    suspend fun deleteMessage(
        @Path("id") id: String
    ) : Message

    //Notification
    @GET("api/Notifications")
    suspend fun getNotification() : List<Notification>

    @GET("api/Notifications/unread-count")
    suspend fun getUnReadNotification() : List<Notification>

    @PUT("api/Notifications/{id}/read")
    suspend fun readNotification(
        @Path("id") id: String
    ) : Notification

    @PUT("api/Notifications/read-all")
    suspend fun readAllNotification()

    @DELETE("api/Notifications/{id}")
    suspend fun deleteNotification(
        @Path("id") id: String
    ) : Notification

    //ChatBot
    @POST("api/chat/new-session")
    suspend fun newChatSession() : ChatItem

    @POST("api/chat/send")
    suspend fun sendChatbotMessage(
        @Body message: Message
    ) : Message

    @GET("api/chat/history/{sessionId}")
    suspend fun getChatSession(
        @Path("sessionId") id: String
    ) : ChatItem

    //ExperienceGeneration
    @POST("api/experience/generate")
    suspend fun experienceGenerate() : String

    //CodeSubmissions
    @GET("api/CodeSubmissions")
    suspend fun getCodeSubmissions() : CodeSubmissionData

    @POST("api/CodeSubmissions")
    suspend fun addCodeSubmissions(
        @Body codeSubmissionData: CodeSubmissionData
    ) : CodeSubmissionData

    @GET("api/CodeSubmissions/{id}")
    suspend fun getCodeSubmissionsDetails(
        @Path("id") id: String
    ) : CodeSubmissionData

    //candidatesProfile
    @POST("api/candidates")
    suspend fun addCandidates(
        @Body candidateData: CandidateData
    ) : CandidateData

    @GET("api/candidates/me")
    suspend fun getCandidates() : CandidateData

    @PUT("api/candidates/me")
    suspend fun editCandidates(
        @Body candidateData: CandidateData
    ) : CandidateData

    //JobApplication
    @POST("api/jobapplications")
    suspend fun addJobApplication(
        @Body jobApplicationData: JobApplicationData
    ) : JobApplicationData

    @GET("api/jobapplications/mine")
    suspend fun getJobApplication() : JobApplicationData

    @GET("api/jobapplications/job/{jobId}")
    suspend fun getJobApplicationDetails(
        @Path("jobId") id: String
    ) : JobApplicationData

    @PUT("api/jobapplications/{id}/status")
    suspend fun getJobApplication(
        @Path("id") id: String
    ) : String

    // jobs
    @GET("api/jobs")
    suspend fun getJobs() : List<JobsData>

    @POST("api/jobs")
    suspend fun addJob(
        @Body jobsData: JobsData
    ) : JobsData

    @GET("api/jobs/{id}")
    suspend fun getJob(
        @Path("id") id: String
    ) : JobsData

    @POST("api/jobs/{id}/close")
    suspend fun closeJob(
        @Path("id") id: String
    )

    @GET("api/jobs/{id}/match")
    suspend fun matchJob(
        @Path("id") id: String
    ) : List<JobsData>

    @GET("api/jobs/{id}/candidates")
    suspend fun candidatesJob(
        @Path("id") id: String
    ) : List<JobsData>

    //Media
    @Multipart
    @POST("api/Media/upload")
    suspend fun uploadImage(
        @Part file : MultipartBody.Part
    ) : String

    @GET("api/Media/post/{postId}")
    suspend fun getImage(
        @Path("postId") id: String
    ) : String

    //postAnalysis
    @POST("api/posts/{postId}/analyze")
    suspend fun getPostAnalysis(
        @Path("postId") id: String
    ) : PostData

    //recruiter
    @POST("api/recruiters")
    suspend fun getRecruiters() : List<RecruiterData>

    @GET("api/recruiters/me")
    suspend fun getMyRecruiters() : List<RecruiterData>

    @PUT("api/recruiters/me")
    suspend fun editRecruiters() : RecruiterData
}