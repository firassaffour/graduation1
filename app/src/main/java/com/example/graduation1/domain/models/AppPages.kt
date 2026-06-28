package com.example.graduation1.domain.models

sealed class AppPages(val route: String) {
    object OnBoardingTabs : AppPages("onBoardingTabs")
    object SignUp : AppPages("signUpScreen")
    object Login : AppPages("loginScreen")
    object CreateAccount : AppPages("createAccountScreen")
    object CreatePost : AppPages("createPostScreen")
    object Post : AppPages("postScreen")
    object MyProfileDetails : AppPages("myProfileDetailsScreen")
    object OtherUsersProfile : AppPages("otherUsersProfileScreen")
    object EditProfile : AppPages("editProfileScreen")
    object StartScreen : AppPages("startScreen")
    object ChatbotStart : AppPages("chatbotStartScreen")
    object Chatbot : AppPages("chatbotScreen")
    object ChatbotHistory : AppPages("chatbotHistoryScreen")
    object Notification : AppPages("notificationScreen")
    object NotificationSettings : AppPages("notificationSettingsScreen")
    object Settings : AppPages("settingScreen")
    object Favourite : AppPages("FavouriteScreen")
    object Saved : AppPages("savedScreen")
    object Language : AppPages("LanguageScreen")
    object Location : AppPages("LocationScreen")
    object Subscription : AppPages("SubscriptionScreen")
    object ReportTabs : AppPages("reportTabs")
    object Password : AppPages("PasswordScreen")
    object AboutApplication : AppPages("AboutApplicationScreen")
    object FAQ : AppPages("FAQScreen")
    object MyRooms : AppPages("MyRoomsScreen")
    object InRooms : AppPages("inRoomsScreen")
    object GroupsList : AppPages("groupsListScreen")
    object Messaging : AppPages("messagingScreen")
    object GroupDetails : AppPages("groupDetailsScreen")
    object AddFriends : AppPages("addFriendsScreen")
    object CreateGroup : AppPages("createGroupScreen")


    object JobDetails : AppPages("job_details")
    object ApplyJob : AppPages("apply_job")
    object OfferJob : AppPages("offer_job")

    object SkillDashboard : AppPages("skill_dashboard")
    object MatchResults : AppPages("match_results")
    object AIRecommendedJobs : AppPages("ai_recommended_jobs")

    object CandidateRanking : AppPages("candidate_ranking")

    object AiProfile : AppPages("aiProfileScreen")
    object CodeReview : AppPages("codeReviewScreen")

    object JobsList         : AppPages("jobsListScreen")
    object Analytics        : AppPages("analyticsScreen")
    object ExperienceGen    : AppPages("experienceGeneratorScreen")
    object CandidateProfile : AppPages("candidateProfileScreen")

    object RecruiterProfile : AppPages("recruiterProfileScreen")


}