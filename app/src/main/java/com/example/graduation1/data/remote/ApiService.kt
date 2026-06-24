package com.example.graduation1.data.remote

import com.example.graduation1.domain.models.requets_response.AuthResponse
import com.example.graduation1.domain.models.requets_response.JobApplyRequest
import com.example.graduation1.domain.models.requets_response.JobRequest
import com.example.graduation1.domain.models.requets_response.LoginRequest
import com.example.graduation1.domain.models.requets_response.PostResponse
import com.example.graduation1.domain.models.requets_response.RecruiterProfileRequest
import com.example.graduation1.domain.models.requets_response.RegisterRequest
import com.example.graduation1.domain.models.requets_response.RegisterResponse
import com.example.graduation1.domain.models.requets_response.UpdateUserRequest
import com.example.graduation1.domain.models.requets_response.ApplicationStatusRequest
import com.example.graduation1.domain.models.requets_response.CandidateProfileRequest
import com.example.graduation1.domain.models.requets_response.CandidateProfileResponse
import com.example.graduation1.domain.models.requets_response.ChatMessageResponse
import com.example.graduation1.domain.models.requets_response.ChatSendRequest
import com.example.graduation1.domain.models.requets_response.ChatSendResponse
import com.example.graduation1.domain.models.requets_response.ChatSessionResponse
import com.example.graduation1.domain.models.requets_response.CodeSubmissionResponse
import com.example.graduation1.domain.models.requets_response.CodeSubmitRequest
import com.example.graduation1.domain.models.requets_response.CodeSubmitResult
import com.example.graduation1.domain.models.requets_response.CommentResponse
import com.example.graduation1.domain.models.requets_response.CommunityMemberItem
import com.example.graduation1.domain.models.requets_response.CommunityMessageResponse
import com.example.graduation1.domain.models.requets_response.CommunityResponse
import com.example.graduation1.domain.models.requets_response.CreateCommentRequest
import com.example.graduation1.domain.models.requets_response.CreateCommunityRequest
import com.example.graduation1.domain.models.requets_response.CreatePostRequest
import com.example.graduation1.domain.models.requets_response.ExperienceRequest
import com.example.graduation1.domain.models.requets_response.ExperienceResponse
import com.example.graduation1.domain.models.requets_response.FollowMessageResponse
import com.example.graduation1.domain.models.requets_response.FollowStatusResponse
import com.example.graduation1.domain.models.requets_response.FollowUserItem
import com.example.graduation1.domain.models.requets_response.JobApplicationResponse
import com.example.graduation1.domain.models.requets_response.JobMatchResponse
import com.example.graduation1.domain.models.requets_response.JobResponse
import com.example.graduation1.domain.models.requets_response.LikeCountResponse
import com.example.graduation1.domain.models.requets_response.LikeStatusResponse
import com.example.graduation1.domain.models.requets_response.MediaResponse
import com.example.graduation1.domain.models.requets_response.MembershipResponse
import com.example.graduation1.domain.models.requets_response.MessageResponse
import com.example.graduation1.domain.models.requets_response.NewSessionRequest
import com.example.graduation1.domain.models.requets_response.NotificationResponse
import com.example.graduation1.domain.models.requets_response.PostAnalysisResponse
import com.example.graduation1.domain.models.requets_response.RecruiterDashboardResponse
import com.example.graduation1.domain.models.requets_response.RecruiterProfileResponse
import com.example.graduation1.domain.models.requets_response.SendMessageRequest
import com.example.graduation1.domain.models.requets_response.ToggleLikeResponse
import com.example.graduation1.domain.models.requets_response.UnreadCountResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
        @Path("id") id : String) : RegisterResponse

    @PUT("api/users/{id}")
    suspend fun editUser(
        @Path("id") id : String,
        @Body updateUserRequest: UpdateUserRequest
    ) : Unit

    @DELETE("api/users/{id}")
    suspend fun deleteUser(
        @Path("id") id : String)

    //follow
    @POST("api/Follow/{userId}")
    suspend fun followUser(
        @Path("userId") id: String
    ) : FollowMessageResponse

    @DELETE("api/Follow/{userId}")
    suspend fun unfollowUser(
        @Path("userId") id: String
    ) : FollowMessageResponse

    @GET("api/Follow/{userId}/followers")
    suspend fun getFollowers(@Path("userId") userId: Int): List<FollowUserItem>

    @GET("api/Follow/{userId}/following")
    suspend fun getFollowing(@Path("userId") userId: Int): List<FollowUserItem>

    @GET("api/Follow/{userId}/status")
    suspend fun getFollowStatus(@Path("userId") userId: Int): FollowStatusResponse


    //Posts
    @GET("api/posts")
    suspend fun getPosts(@Query("communityId") communityId: Int? = null): List<PostResponse>

    @GET("api/posts/{id}")
    suspend fun getPostDetails(
        @Path("id") id: String) : PostResponse

    @POST("api/posts")
    suspend fun createPost(@Body request: CreatePostRequest): PostResponse

    @PUT("api/posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body request: CreatePostRequest
    )

    @DELETE("api/posts/{id}")
    suspend fun deletePost(
        @Path("id") id: String)

    //comments
    @POST("api/Comments")
    suspend fun createComment(@Body request: CreateCommentRequest): CommentResponse

    @GET("api/Comments/post/{postId}")
    suspend fun getCommentsByPost(@Path("postId") postId: Int): List<CommentResponse>

    @GET("api/Comments/{id}")
    suspend fun getCommentDetails(
        @Path("id") id: String) : CommentResponse

    @PUT("api/Comments/{id}")
    suspend fun updateComment(
        @Path("id") id: Int,
        @Body request: CreateCommentRequest
    )

    @DELETE("api/Comments/{id}")
    suspend fun deleteComment(@Path("id") id: Int)

    //Like
    @POST("api/Likes/toggle/{postId}")
    suspend fun toggleLike(@Path("postId") postId: Int): ToggleLikeResponse

    @GET("api/Likes/count/{postId}")
    suspend fun getLikeCount(@Path("postId") postId: Int): LikeCountResponse

    @GET("api/Likes/status/{postId}")
    suspend fun getLikeStatus(@Path("postId") postId: Int): LikeStatusResponse

    // savedPosts
    @GET("api/SavedPosts")
    suspend fun getSavedPosts() : List<PostResponse>

    @POST("api/SavedPosts/{postId}")
    suspend fun savePost(@Path("postId") postId: Int)

    @DELETE("api/SavedPosts/{postId}")
    suspend fun unsavePost(@Path("postId") postId: Int)

    //Communities
    @GET("api/Communities")
    suspend fun getCommunities(): List<CommunityResponse>

    @POST("api/Communities")
    suspend fun createCommunity(@Body request: CreateCommunityRequest): CommunityResponse

    @POST("api/Communities/{communityId}/join")
    suspend fun joinCommunity(@Path("communityId") id: Int): CommunityMessageResponse

    @DELETE("api/Communities/{communityId}/leave")
    suspend fun leaveCommunity(@Path("communityId") id: Int): CommunityMessageResponse

    @GET("api/Communities/{communityId}/members")
    suspend fun getCommunityMembers(@Path("communityId") id: Int): List<CommunityMemberItem>

    @GET("api/Communities/{communityId}/membership")
    suspend fun getCommunityMembership(@Path("communityId") id: Int): MembershipResponse

    //Messages
    @GET("api/Messages/inbox")
    suspend fun getInbox(): List<MessageResponse>

    @GET("api/Messages/conversation/{otherUserId}")
    suspend fun getConversation(@Path("otherUserId") otherUserId: Int): List<MessageResponse>

    @POST("api/Messages")
    suspend fun sendMessage(@Body request: SendMessageRequest): MessageResponse

    @DELETE("api/Messages/{id}")
    suspend fun deleteMessage(@Path("id") id: Int)

    //Notification
    @GET("api/Notifications")
    suspend fun getNotifications(): List<NotificationResponse>

    @GET("api/Notifications/unread-count")
    suspend fun getUnreadCount(): UnreadCountResponse

    @PUT("api/Notifications/{id}/read")
    suspend fun markNotificationRead(@Path("id") id: Int)

    @PUT("api/Notifications/read-all")
    suspend fun markAllNotificationsRead()

    @DELETE("api/Notifications/{id}")
    suspend fun deleteNotification(@Path("id") id: Int)

    //ChatBot
    @POST("api/chat/new-session")
    suspend fun newChatSession(@Body request: NewSessionRequest = NewSessionRequest()): ChatSessionResponse

    @POST("api/chat/send")
    suspend fun sendChatbotMessage(@Body request: ChatSendRequest): ChatSendResponse

    /** Returns list of ChatMessage objects */
    @GET("api/chat/history/{sessionId}")
    suspend fun getChatHistory(@Path("sessionId") sessionId: Int): List<ChatMessageResponse>

    //ExperienceGeneration
    @POST("api/experience/generate")
    suspend fun generateExperience(@Body request: ExperienceRequest = ExperienceRequest()): ExperienceResponse

    //CodeSubmissions
    @GET("api/CodeSubmissions")
    suspend fun getCodeSubmissions(): List<CodeSubmissionResponse>

    @GET("api/CodeSubmissions/{id}")
    suspend fun getCodeSubmissionById(@Path("id") id: Int): CodeSubmissionResponse

    @POST("api/CodeSubmissions")
    suspend fun submitCode(@Body request: CodeSubmitRequest): CodeSubmitResult

    //candidatesProfile
    @POST("api/candidates")
    suspend fun createCandidateProfile(@Body request: CandidateProfileRequest): CandidateProfileResponse

    @GET("api/candidates/me")
    suspend fun getMyCandidateProfile(): CandidateProfileResponse

    @PUT("api/candidates/me")
    suspend fun updateCandidateProfile(@Body request: CandidateProfileRequest): CandidateProfileResponse

    //JobApplication
    @POST("api/jobapplications")
    suspend fun applyToJob(@Body request: JobApplyRequest): JobApplicationResponse

    @GET("api/jobapplications/mine")
    suspend fun getMyApplications(): List<JobApplicationResponse>

    @GET("api/jobapplications/job/{jobId}")
    suspend fun getApplicationsForJob(@Path("jobId") jobId: Int): List<JobApplicationResponse>

    @PUT("api/jobapplications/{id}/status")
    suspend fun updateApplicationStatus(
        @Path("id") id: Int,
        @Body request: ApplicationStatusRequest
    ): JobApplicationResponse

    // jobs
    @GET("api/jobs")
    suspend fun getJobs(
        @Query("location") location: String? = null,
        @Query("skill")    skill: String? = null,
        @Query("q")        q: String? = null
    ): List<JobResponse>

    @GET("api/jobs/{id}")
    suspend fun getJobById(@Path("id") id: Int): JobResponse

    @POST("api/jobs")
    suspend fun createJob(@Body request: JobRequest): JobResponse

    @POST("api/jobs/{id}/close")
    suspend fun closeJob(@Path("id") id: Int)

    @GET("api/jobs/{id}/match")
    suspend fun getJobMatch(@Path("id") id: Int): JobMatchResponse

    @GET("api/jobs/{id}/candidates")
    suspend fun getJobCandidates(@Path("id") id: Int): RecruiterDashboardResponse

    //Media
    @Multipart
    @POST("api/Media/upload")
    suspend fun uploadMedia(
        @Part file: MultipartBody.Part,
        @Query("postId")    postId: Int? = null,
        @Query("messageId") messageId: Int? = null,
        @Query("commentId") commentId: Int? = null
    ): MediaResponse

    @GET("api/Media/post/{postId}")
    suspend fun getMediaByPost(@Path("postId") postId: Int): List<MediaResponse>

    //postAnalysis
    @POST("api/posts/{postId}/analyze")
    suspend fun analyzePost(@Path("postId") postId: Int): PostAnalysisResponse

    //recruiter
    @POST("api/recruiters")
    suspend fun createRecruiterProfile(@Body request: RecruiterProfileRequest): RecruiterProfileResponse

    @GET("api/recruiters/me")
    suspend fun getMyRecruiterProfile(): RecruiterProfileResponse

    @PUT("api/recruiters/me")
    suspend fun updateRecruiterProfile(@Body request: RecruiterProfileRequest): RecruiterProfileResponse
}