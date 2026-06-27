package com.example.graduation1.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.data.repository.AuthRepository
import com.example.graduation1.data.repository.ChatbotRepository
import com.example.graduation1.data.repository.MediaRepository
import com.example.graduation1.data.repository.UserRepository
import com.example.graduation1.domain.models.Message
import com.example.graduation1.domain.models.User
import com.example.graduation1.domain.models.requets_response.AiResponseItem
import com.example.graduation1.domain.models.requets_response.CandidateProfileRequest
import com.example.graduation1.domain.models.requets_response.CandidateProfileResponse
import com.example.graduation1.domain.models.requets_response.ChatMessageResponse
import com.example.graduation1.domain.models.requets_response.ChatSessionResponse
import com.example.graduation1.domain.models.requets_response.CodeSubmissionResponse
import com.example.graduation1.domain.models.requets_response.CodeSubmitRequest
import com.example.graduation1.domain.models.requets_response.CodeSubmitResult
import com.example.graduation1.domain.models.requets_response.ExperienceResponse
import com.example.graduation1.domain.models.requets_response.JobApplicationResponse
import com.example.graduation1.domain.models.requets_response.JobMatchResponse
import com.example.graduation1.domain.models.requets_response.JobRequest
import com.example.graduation1.domain.models.requets_response.JobResponse
import com.example.graduation1.domain.models.requets_response.PostAnalysisResponse
import com.example.graduation1.domain.models.requets_response.RecruiterDashboardResponse
import com.example.graduation1.domain.models.requets_response.RecruiterProfileRequest
import com.example.graduation1.domain.models.requets_response.RecruiterProfileResponse
import com.example.graduation1.messageList
import com.example.graduation1.user
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatbotViewModel(private val repository: ChatbotRepository, private val userRepository: UserRepository) : ViewModel() {

    /** Messages shown in the current conversation (user + assistant bubbles). */
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    /** The text the user is typing right now. */
    private val _messageText = MutableStateFlow("")
    val messageText = _messageText.asStateFlow()

    /** True while we are waiting for the AI to reply. */
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    /** Non-null when something went wrong; shown as a snackbar / toast. */
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    /** All sessions for the history screen. */
    private val _sessions = MutableStateFlow<List<ChatSessionResponse>>(emptyList())
    val sessions = _sessions.asStateFlow()

    /** The session that is currently open. */
    private val _activeSession = MutableStateFlow<ChatSessionResponse?>(null)
    val activeSession = _activeSession.asStateFlow()

    /** The logged-in user (needed to decide which bubble side to use). */
    private val _currentUser = MutableStateFlow(User())
    val currentUser = _currentUser.asStateFlow()

    /** The AI feedback text after a code review submission. */
    private val _codeReviewResult = MutableStateFlow<AiResponseItem?>(null)
    val codeReviewResult = _codeReviewResult.asStateFlow()

    /** True while the review is being processed. */
    private val _isReviewing = MutableStateFlow(false)
    val isReviewing = _isReviewing.asStateFlow()

    // ── Experience generator ──────────────────────────────────────────────────
    private val _experienceResult = MutableStateFlow<ExperienceResponse?>(null)
    val experienceResult = _experienceResult.asStateFlow()

    private val _isGeneratingExp = MutableStateFlow(false)
    val isGeneratingExp = _isGeneratingExp.asStateFlow()

    // ── Post analysis ─────────────────────────────────────────────────────────
    private val _postAnalysis = MutableStateFlow<PostAnalysisResponse?>(null)
    val postAnalysis = _postAnalysis.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing = _isAnalyzing.asStateFlow()

    // ── Candidate profile ─────────────────────────────────────────────────────
    private val _candidateProfile = MutableStateFlow<CandidateProfileResponse?>(null)
    val candidateProfile = _candidateProfile.asStateFlow()

    private val _isSavingCandidate = MutableStateFlow(false)
    val isSavingCandidate = _isSavingCandidate.asStateFlow()

    // ── Jobs ──────────────────────────────────────────────────────────────────
    private val _jobs = MutableStateFlow<List<JobResponse>>(emptyList())
    val jobs = _jobs.asStateFlow()

    private val _selectedJob = MutableStateFlow<JobResponse?>(null)
    val selectedJob = _selectedJob.asStateFlow()

    private val _jobMatch = MutableStateFlow<JobMatchResponse?>(null)
    val jobMatch = _jobMatch.asStateFlow()

    private val _isLoadingJobs = MutableStateFlow(false)
    val isLoadingJobs = _isLoadingJobs.asStateFlow()

    // ── Job applications ──────────────────────────────────────────────────────
    private val _myApplications = MutableStateFlow<List<JobApplicationResponse>>(emptyList())
    val myApplications = _myApplications.asStateFlow()

    private val _isApplying = MutableStateFlow(false)
    val isApplying = _isApplying.asStateFlow()

    private val _applySuccess = MutableStateFlow(false)
    val applySuccess = _applySuccess.asStateFlow()

    // ── Recruiter ─────────────────────────────────────────────────────────────
    private val _recruiterProfile = MutableStateFlow<RecruiterProfileResponse?>(null)
    val recruiterProfile = _recruiterProfile.asStateFlow()

    private val _jobCandidates = MutableStateFlow<RecruiterDashboardResponse?>(null)
    val jobCandidates = _jobCandidates.asStateFlow()

    private val _isSavingRecruiter = MutableStateFlow(false)
    val isSavingRecruiter = _isSavingRecruiter.asStateFlow()


    init {
        loadCurrentUser()
        loadJobs()
        loadMyApplications()
        loadCandidateProfile()
        loadRecruiterProfile()
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


    fun updateMessageText(messageText: String){
        _messageText.value = messageText
    }



    // ── Init ────────────────────────────────────────────────────────────────

    private fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                _currentUser.value = userRepository.getCurrentUser()
            } catch (e: Exception) {
                Log.e("ChatbotVM", "loadCurrentUser: ${e.message}")
            }
        }
    }

    // ── Session management ──────────────────────────────────────────────────

    /**
     * Call this when the user taps "New Chat" or opens the chatbot screen
     * for the first time. It creates a fresh session on the backend and
     * clears the message list.
     */
    fun startNewSession(title: String? = null) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val session = repository.newSession(title)
                _activeSession.value = session
                _messages.value = emptyList()
                // Add the new session to the history list
                _sessions.value = listOf(session) + _sessions.value
                Log.d("ChatbotVM", "New session: ${session.sessionID}")
            } catch (e: Exception) {
                _error.value = "Could not start a new chat. Check your connection."
                Log.e("ChatbotVM", "startNewSession: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Open a past session from the history screen and load its messages.
     */
    fun openSession(session: ChatSessionResponse) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _activeSession.value = session
                val history = repository.getHistory(session.sessionID)
                _messages.value = history.map { it.toUiMessage() }
                Log.d("ChatbotVM", "Loaded ${history.size} messages for session ${session.sessionID}")
            } catch (e: Exception) {
                _error.value = "Could not load chat history."
                Log.e("ChatbotVM", "openSession: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ── Messaging ───────────────────────────────────────────────────────────

    /**
     * Send the current [messageText] to the backend and append both the
     * user bubble and the AI reply to [messages].
     *
     * If no session is open yet, we create one automatically.
     */
    fun sendMessage() {
        val text = _messageText.value.trim()
        if (text.isEmpty()) return

        viewModelScope.launch {
            try {
                Log.e("CHATBOT", "1", )
                // 1. Ensure we have an active session
                val session = _activeSession.value ?: run {
                    Log.e("CHATBOT", "2", )
                    val s = repository.newSession(_messageText.value)
                    Log.e("CHATBOT", "3 ${s.sessionID}", )
                    _activeSession.value = s
                    _sessions.value = listOf(s) + _sessions.value
                    s
                }

                Log.e("CHATBOT", "4", )

                // 2. Optimistically add the user bubble immediately
                val userBubble = Message(
                    messageId  = "tmp_${System.currentTimeMillis()}",
                    text       = text,
                    senderId   = _currentUser.value.id,
                    createdAt  = System.currentTimeMillis()
                )
                _messages.value = _messages.value + userBubble
                _messageText.value = ""

                // 3. Show loading indicator
                _isLoading.value = true

                // 4. Send to backend
                val response = repository.sendMessage(session.sessionID, text)

                Log.e("CHATBOT", "5 $response", )

                if (response.error != null) {
                    _error.value = "AI error: ${response.error}"
                    return@launch
                }
                Log.e("ChatbotVM", "sendMessage: ${_error.value}")

                // 5. Append the AI reply bubble
                val reply = response.reply ?: "(no reply)"
                val aiBubble = Message(
                    messageId = "ai_${System.currentTimeMillis()}",
                    text      = reply,
                    senderId  = "AI",   // anything that is NOT the user's id
                    createdAt = System.currentTimeMillis()
                )
                _messages.value = _messages.value + aiBubble

                Log.d("ChatbotVM", "sendMessage OK, reply: $reply")

            } catch (e: Exception) {
                _error.value = "Could not reach the server. Are you online?"
                Log.e("ChatbotVM", "sendMessage: ${e.message}")
                Log.e("ChatbotVM", "sendMessage: ${_error.value}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ── Text field ──────────────────────────────────────────────────────────

    fun clearError() {
        _error.value = null
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    /** Convert the backend ChatMessageResponse to the local Message UI model. */
    private fun ChatMessageResponse.toUiMessage() = Message(
        messageId = messageID.toString(),
        text      = content ?: "",
        // role is "user" or "assistant" – map "user" to the actual user's id,
        // everything else to "AI" so the bubble appears on the left.
        senderId  = if (role == "user") _currentUser.value.id else "AI",
        createdAt = System.currentTimeMillis() // we don't parse the date string here
    )

    fun getCodeSubmissions() {
        viewModelScope.launch {
            try {
                val response = repository.getCodeSubmissions()
                Log.d("API", "getCodeSubmissions: $response")
            }
            catch (e : Exception){
                Log.e("API", "getCodeSubmissions: ${e.message}", )
            }

        }
    }

    fun getCodeSubmissionById(id: Int){
        viewModelScope.launch {
            try {
                val response = repository.getCodeSubmissionById(id)
                Log.d("API", "getCodeSubmissionById: $response")
            }
            catch (e : Exception){
                Log.e("API", "getCodeSubmissionById: ${e.message}", )
            }
        }
    }


    /**
     * Submit code for AI review. Optionally link it to a [postId].
     * When the result comes back, [codeReviewResult] will be updated —
     * collect it in your UI to show the feedback.
     */
    fun submitCodeForReview(code: String, language: String? = null, postId: Int? = null) {
        if (code.isBlank()) {
            _error.value = "Please write some code first."
            return
        }
        viewModelScope.launch {
            try {
                _isReviewing.value = true
                _codeReviewResult.value = null

                val result = repository.submitCode(
                    com.example.graduation1.domain.models.requets_response.CodeSubmitRequest(
                        code     = code,
                        language = language,
                        postID   = 7
                    )
                )

                Log.d("ChatbotVM", "submitCode raw result: $result")

                // KEY FIX: aiFeedback may be null if Gson can't match "aIFeedback"
                // (capital I). Fall back to the first item in submission.aiResponses.
                val feedback = result.aiFeedback
                    ?: result.submission.aiResponses?.firstOrNull()

                if (feedback == null) {
                    _error.value = "The server returned no AI feedback. Try again."
                    Log.e("ChatbotVM", "submitCodeForReview: aiFeedback is null in result=$result")
                    return@launch
                }

                _codeReviewResult.value = feedback
                Log.d("ChatbotVM", "Code review OK: ${feedback.content}")

            } catch (e: Exception) {
                _error.value = "Code review failed: ${e.message}"
                Log.e("ChatbotVM", "submitCodeForReview: ${e.message}")
            } finally {
                _isReviewing.value = false
            }
        }
    }

    /** Call after displaying the review so the UI goes back to idle. */
    fun clearCodeReview() { _codeReviewResult.value = null }

    // ═══════════════════════════════════════════════════════════════════════
    //  EXPERIENCE GENERATOR
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Call this when the user taps "Generate" in ExperienceGeneratorScreen.
     * Pass the role the user typed. Result goes into [experienceResult].
     */
    fun generateExperience(targetRole: String?) {
        viewModelScope.launch {
            try {
                _isGeneratingExp.value = true; _experienceResult.value = null
                val result = repository.generateExperience(targetRole?.ifBlank { null })
                _experienceResult.value = result
                Log.d("ChatbotVM", "generateExperience OK: ${result.cvBullets.size} bullets")
            } catch (e: Exception) {
                val msg = e.message ?: ""
                _error.value = if (msg.contains("502") || msg.contains("50")) {
                    "You need to create posts with code snippets first. The AI builds your skill profile from your posts."
                } else "Could not generate experience: $msg"
                Log.e("ChatbotVM", "generateExperience: $msg")
            } finally { _isGeneratingExp.value = false }
        }
    }
    fun clearExperience() { _experienceResult.value = null }

    // ═══════════════════════════════════════════════════════════════════════
    //  POST ANALYSIS
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Analyze a post's code content.
     * Call from PostScreen when the user taps an "Analyze" button on a post.
     * Result goes into [postAnalysis].
     */
    fun analyzePost(postId: Int) {
        viewModelScope.launch {
            try {
                _isAnalyzing.value = true
                _postAnalysis.value = null
                val result = repository.analyzePost(postId)
                _postAnalysis.value = result
                Log.d("ChatbotVM", "analyzePost OK: score=${result.score}")
            } catch (e: Exception) {
                _error.value = "Analysis failed: ${e.message}"
                Log.e("ChatbotVM", "analyzePost: ${e.message}")
            } finally { _isAnalyzing.value = false }
        }
    }

    fun clearPostAnalysis() { _postAnalysis.value = null }

    // ═══════════════════════════════════════════════════════════════════════
    //  CANDIDATE PROFILE
    // ═══════════════════════════════════════════════════════════════════════

    private fun loadCandidateProfile() {
        viewModelScope.launch {
            try {
                _candidateProfile.value = repository.getMyCandidateProfile()
                Log.d("API", "loadCandidateProfile success: $_candidateProfile.")
            } catch (e: Exception) {
                // 404 is normal for first-time users — don't show error
                Log.d("ChatbotVM", "No candidate profile yet: ${e.message}")
            }
        }
    }

    /**
     * Create or update the candidate profile depending on whether one exists.
     * UI just calls this with the filled-in form values.
     */
    fun saveCandidateProfile(
        country: String,
        governorate: String,
        yearsOfExperience: Int,
        preferredRole: String,
        bio: String,
        availabilityStatus: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                _isSavingCandidate.value = true
                val request = CandidateProfileRequest(
                    country            = country.ifBlank { null },
                    governorate        = governorate.ifBlank { null },
                    yearsOfExperience  = yearsOfExperience,
                    preferredRole      = preferredRole.ifBlank { null },
                    bio                = bio.ifBlank { null },
                    availabilityStatus = availabilityStatus.ifBlank { null }
                )
                val result = if (_candidateProfile.value == null)
                    repository.createCandidateProfile(request)
                else
                    repository.updateCandidateProfile(request)

                _candidateProfile.value = result
                onSuccess()
                Log.d("ChatbotVM", "saveCandidateProfile OK")
            } catch (e: Exception) {
                _error.value = "Could not save profile: ${e.message}"
                Log.e("ChatbotVM", "saveCandidateProfile: ${e.message}")
            } finally { _isSavingCandidate.value = false }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  JOBS
    // ═══════════════════════════════════════════════════════════════════════

    private fun loadJobs(q: String? = null, location: String? = null, skill: String? = null) {
        viewModelScope.launch {
            try {
                _isLoadingJobs.value = true
                _jobs.value = repository.getJobs(location, skill, q)
                Log.d("ChatbotVM", "loadJobs OK: ${_jobs.value.size}")
            } catch (e: Exception) {
                Log.e("ChatbotVM", "loadJobs: ${e.message}")
            } finally { _isLoadingJobs.value = false }
        }
    }

    /** Call when the user types in the search field or picks a filter. */
    fun searchJobs(q: String? = null, location: String? = null, skill: String? = null) =
        loadJobs(q, location, skill)

    fun selectJob(job: JobResponse?) {
        _selectedJob.value = job
        if (job != null) loadJobMatch(job.jobID)
    }

    private fun loadJobMatch(jobId: Int) {
        viewModelScope.launch {
            try {
                _jobMatch.value = repository.getJobMatch(jobId)
            } catch (e: Exception) {
                Log.e("ChatbotVM", "loadJobMatch: ${e.message}")
            }
        }
    }

    /** Recruiter: post a new job. */
    fun createJob(
        title: String,
        description: String,
        location: String,
        experienceLevel: String,
        skills: List<String>,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                _isLoadingJobs.value = true
                val result = repository.createJob(
                    JobRequest(
                        title           = title,
                        description     = description.ifBlank { null },
                        location        = location.ifBlank { null },
                        experienceLevel = experienceLevel.ifBlank { null },
                        requiredSkills  = skills.ifEmpty { null }
                    )
                )
                _jobs.value = listOf(result) + _jobs.value
                onSuccess()
                Log.d("ChatbotVM", "createJob OK: ${result.jobID}")
            } catch (e: Exception) {
                _error.value = "Could not post job: ${e.message}"
                Log.e("ChatbotVM", "createJob: ${e.message}")
            } finally { _isLoadingJobs.value = false }
        }
    }

    /** Recruiter: close a job posting. */
    fun closeJob(jobId: Int) {
        viewModelScope.launch {
            try {
                repository.closeJob(jobId)
                _jobs.value = _jobs.value.map {
                    if (it.jobID == jobId) it.copy(isOpen = false) else it
                }
            } catch (e: Exception) {
                _error.value = "Could not close job: ${e.message}"
                Log.e("ChatbotVM", "closeJob: ${e.message}")
            }
        }
    }

    /** Recruiter: load ranked candidates for a job. */
    fun loadJobCandidates(jobId: Int) {
        viewModelScope.launch {
            try {
                _jobCandidates.value = repository.getJobCandidates(jobId)
                Log.d("ChatbotVM", "loadJobCandidates OK: ${_jobCandidates.value?.totalCandidates}")
            } catch (e: Exception) {
                Log.e("ChatbotVM", "loadJobCandidates: ${e.message}")
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  JOB APPLICATIONS
    // ═══════════════════════════════════════════════════════════════════════

    private fun loadMyApplications() {
        viewModelScope.launch {
            try {
                _myApplications.value = repository.getMyApplications()
            } catch (e: Exception) {
                Log.d("ChatbotVM", "loadMyApplications: ${e.message}")
            }
        }
    }

    /**
     * Apply to a job. [jobId] comes from [JobResponse.jobID].
     * After success [applySuccess] flips to true — reset it after showing confirmation.
     */
    fun applyToJob(jobId: Int, coverNote: String = "") {
        viewModelScope.launch {
            try {
                _isApplying.value = true
                val result = repository.applyToJob(jobId, coverNote.ifBlank { null })
                _myApplications.value = _myApplications.value + result
                _applySuccess.value = true
                Log.d("ChatbotVM", "applyToJob OK: ${result.applicationID}")
            } catch (e: Exception) {
                _error.value = "Application failed: ${e.message}"
                Log.e("ChatbotVM", "applyToJob: ${e.message}")
            } finally { _isApplying.value = false }
        }
    }

    fun resetApplySuccess() { _applySuccess.value = false }

    fun hasApplied(jobId: Int) = _myApplications.value.any { it.jobID == jobId }

    // ═══════════════════════════════════════════════════════════════════════
    //  RECRUITER PROFILE
    // ═══════════════════════════════════════════════════════════════════════

    private fun loadRecruiterProfile() {
        viewModelScope.launch {
            try {
                _recruiterProfile.value = repository.getMyRecruiterProfile()
            } catch (e: Exception) {
                Log.d("ChatbotVM", "No recruiter profile yet: ${e.message}")
            }
        }
    }

    fun saveRecruiterProfile(
        companyName: String,
        companyWebsite: String,
        location: String,
        about: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                _isSavingRecruiter.value = true
                val request = RecruiterProfileRequest(
                    companyName    = companyName,
                    companyWebsite = companyWebsite.ifBlank { null },
                    location       = location.ifBlank { null },
                    about          = about.ifBlank { null }
                )
                val result = if (_recruiterProfile.value == null)
                    repository.createRecruiterProfile(request)
                else
                    repository.updateRecruiterProfile(request)

                _recruiterProfile.value = result
                onSuccess()
                Log.d("ChatbotVM", "saveRecruiterProfile OK")
            } catch (e: Exception) {
                _error.value = "Could not save recruiter profile: ${e.message}"
                Log.e("ChatbotVM", "saveRecruiterProfile: ${e.message}")
            } finally { _isSavingRecruiter.value = false }
        }
    }

}