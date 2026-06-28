package com.example.graduation1.data.repository

import com.example.graduation1.data.remote.ApiService
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
import com.example.graduation1.domain.models.requets_response.ExperienceRequest
import com.example.graduation1.domain.models.requets_response.ExperienceResponse
import com.example.graduation1.domain.models.requets_response.JobApplicationResponse
import com.example.graduation1.domain.models.requets_response.JobApplyRequest
import com.example.graduation1.domain.models.requets_response.JobMatchResponse
import com.example.graduation1.domain.models.requets_response.JobRequest
import com.example.graduation1.domain.models.requets_response.JobResponse
import com.example.graduation1.domain.models.requets_response.NewSessionRequest
import com.example.graduation1.domain.models.requets_response.PostAnalysisResponse
import com.example.graduation1.domain.models.requets_response.RecruiterDashboardResponse
import com.example.graduation1.domain.models.requets_response.RecruiterProfileRequest
import com.example.graduation1.domain.models.requets_response.RecruiterProfileResponse

class ChatbotRepository(private val api : ApiService) {

    suspend fun newSession(title: String? = null): ChatSessionResponse =
        api.newChatSession(NewSessionRequest(title))

    suspend fun sendMessage(sessionId: Int, message: String): ChatSendResponse =
        api.sendChatbotMessage(ChatSendRequest(sessionId, message))

    suspend fun getHistory(sessionId: Int): List<ChatMessageResponse> =
        api.getChatHistory(sessionId)

    // ── Code Submissions ─────────────────────────────────────────────────────
    suspend fun getCodeSubmissions(): List<CodeSubmissionResponse> =
        api.getCodeSubmissions()

    suspend fun getCodeSubmissionById(id: Int): CodeSubmissionResponse =
        api.getCodeSubmissionById(id)

    suspend fun submitCode(request: CodeSubmitRequest): CodeSubmitResult { return api.submitCode(request)}

    // ── Experience Generation ────────────────────────────────────────────────
    suspend fun generateExperience(targetRole: String? = null): ExperienceResponse =
        api.generateExperience(ExperienceRequest(targetRole))

    // ── Post Analysis ────────────────────────────────────────────────────────
    suspend fun analyzePost(postId: Int): PostAnalysisResponse =
        api.analyzePost(postId)

    // ── Candidate Profile ────────────────────────────────────────────────────
    suspend fun createCandidateProfile(request: CandidateProfileRequest): CandidateProfileResponse =
        api.createCandidateProfile(request)

    suspend fun getMyCandidateProfile(): CandidateProfileResponse =
        api.getMyCandidateProfile()

    suspend fun updateCandidateProfile(request: CandidateProfileRequest): CandidateProfileResponse =
        api.updateCandidateProfile(request)

    // ── Jobs ─────────────────────────────────────────────────────────────────
    suspend fun getJobs(location: String? = null, skill: String? = null, q: String? = null): List<JobResponse> =
        api.getJobs(location, skill, q)

    suspend fun getJobById(id: Int): JobResponse =
        api.getJobById(id)

    suspend fun createJob(request: JobRequest): JobResponse =
        api.createJob(request)

    suspend fun closeJob(id: Int) =
        api.closeJob(id)

    suspend fun getJobMatch(id: Int): JobMatchResponse =
        api.getJobMatch(id)

    suspend fun getJobCandidates(id: Int): RecruiterDashboardResponse =
        api.getJobCandidates(id)

    // ── Job Applications ─────────────────────────────────────────────────────
    suspend fun applyToJob(jobId: Int, coverNote: String? = null): JobApplicationResponse =
        api.applyToJob(JobApplyRequest(jobId, coverNote))

    suspend fun getMyApplications(): List<JobApplicationResponse> =
        api.getMyApplications()

    suspend fun getApplicationsForJob(jobId: Int): List<JobApplicationResponse> =
        api.getApplicationsForJob(jobId)

    suspend fun updateApplicationStatus(id: Int, status: String): JobApplicationResponse =
        api.updateApplicationStatus(id, ApplicationStatusRequest(status))

    // ── Recruiter Profile ────────────────────────────────────────────────────
    suspend fun createRecruiterProfile(request: RecruiterProfileRequest): RecruiterProfileResponse =
        api.createRecruiterProfile(request)

    suspend fun getMyRecruiterProfile(): RecruiterProfileResponse =
        api.getMyRecruiterProfile()

    suspend fun updateRecruiterProfile(request: RecruiterProfileRequest): RecruiterProfileResponse =
        api.updateRecruiterProfile(request)

}