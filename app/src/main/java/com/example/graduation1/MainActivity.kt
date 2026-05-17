package com.example.graduation1

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.graduation1.domain.models.AppPages
import com.example.graduation1.domain.models.BottomNavItem
import com.example.graduation1.domain.models.BottomNavigationBar
import com.example.graduation1.ui.screens.authentication.CreateAccountScreen
import com.example.graduation1.ui.screens.authentication.LoginScreen
import com.example.graduation1.ui.screens.authentication.OnBoardingTabs
import com.example.graduation1.ui.screens.messaging.ChatTabs
import com.example.graduation1.ui.screens.chatbot.ChatbotScreen
import com.example.graduation1.ui.screens.chatbot.ChatbotStartScreen
import com.example.graduation1.ui.screens.createpost.CreatePostScreen
import com.example.graduation1.ui.screens.profiles.EditProfileScreen
import com.example.graduation1.ui.screens.settings.FAQScreen
import com.example.graduation1.ui.screens.friends.FriendsScreen
import com.example.graduation1.ui.screens.home.HomeScreen
import com.example.graduation1.ui.screens.groups.InRoomsScreen
import com.example.graduation1.ui.screens.authentication.SignUpScreen
import com.example.graduation1.ui.screens.messaging.MessagingScreen
import com.example.graduation1.ui.screens.profiles.MyProfileDetailsScreen
import com.example.graduation1.ui.screens.profiles.MyProfileScreen
import com.example.graduation1.ui.screens.groups.MyRoomsScreen
import com.example.graduation1.ui.screens.notification.NotificationScreen
import com.example.graduation1.ui.screens.settings.NotificationSettingsScreen
import com.example.graduation1.ui.screens.profiles.OtherUsersProfileScreen
import com.example.graduation1.ui.screens.settings.SettingsScreen
import com.example.graduation1.ui.screens.authentication.StartScreen
import com.example.graduation1.ui.screens.home.PostScreen
import com.example.graduation1.ui.screens.settings.AboutApplicationScreen
import com.example.graduation1.ui.screens.settings.SavedScreen
import com.example.graduation1.ui.screens.settings.FavouriteScreen
import com.example.graduation1.ui.screens.settings.LanguageScreen
import com.example.graduation1.ui.screens.settings.LocationScreen
import com.example.graduation1.ui.screens.settings.PasswordScreen
import com.example.graduation1.ui.screens.settings.ReportTabs
import com.example.graduation1.ui.screens.settings.SubscriptionScreen
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.primaryRed
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Locale

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        language = prefs.getString("lang", "en") ?: "en"
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = this.resources.configuration
        config.setLocale(locale)

        this.resources.updateConfiguration(config, this.resources.displayMetrics)

        darkMode = prefs.getBoolean("dark", false)

        setContent {
            Graduation1Theme(darkTheme = darkMode, dynamicColor = false) {
                val systemUiController = rememberSystemUiController()
                val navController = rememberNavController()
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                SideEffect {
                    when {
                        darkMode ->{
                            systemUiController.setStatusBarColor(Color(23, 21, 21, 255))
                            systemUiController.setNavigationBarColor(Color(23, 21, 21, 255))
                        }
                        currentRoute?.startsWith(AppPages.MyProfileDetails.route) == true || currentRoute?.startsWith(AppPages.OtherUsersProfile.route) == true ->{
                            systemUiController.setStatusBarColor(Color(29, 56, 58))
                            systemUiController.setNavigationBarColor(Color.White)
                        }
                        currentRoute?.startsWith(AppPages.OnBoardingTabs.route) == true ->{
                            systemUiController.setStatusBarColor(primaryRed)
                            systemUiController.setNavigationBarColor(primaryRed)
                        }
                        else -> {
                            systemUiController.setStatusBarColor(Color.White)
                            systemUiController.setNavigationBarColor(Color.White)
                        }
                    }
                }
                MainScreen(navController, currentRoute)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun MainScreen(navController: NavHostController, currentRoute : String?) {

        val showBottomBar = currentRoute in BottomNavItem.items.map { it.route }


        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(navController)
                }
            }
        ) { padding ->

            NavHost(
                navController = navController,
                startDestination = AppPages.OnBoardingTabs.route,
                modifier = Modifier.padding(padding)
            ) {
                composable(AppPages.StartScreen.route) { StartScreen(navController) }
                composable(AppPages.OnBoardingTabs.route) { OnBoardingTabs(navController) }
                composable(BottomNavItem.Home.route) { HomeScreen(navController) }
                composable(BottomNavItem.Chat.route) { ChatTabs(navController) }
                composable(BottomNavItem.Create.route) { CreatePostScreen(navController) }
                composable(AppPages.MyRooms.route) { MyRoomsScreen(navController) }
                composable(BottomNavItem.Friends.route) { FriendsScreen(navController) }
                composable(BottomNavItem.Profile.route) { HomeScreen(navController) }
                composable(AppPages.SignUp.route) { SignUpScreen(navController) }
                composable(AppPages.Login.route) { LoginScreen(navController) }
                composable(AppPages.CreateAccount.route) { CreateAccountScreen(navController) }
                composable("${AppPages.Post.route}/{postId}", arguments = listOf(navArgument("postId") {type = NavType.StringType})){ backStack ->
                val postId = backStack.arguments?.getString("postId")
                    PostScreen(navController, postId!!)
                } // Composable
                composable(BottomNavItem.Profile.route) { MyProfileScreen(navController) }
                composable(AppPages.EditProfile.route) { EditProfileScreen(navController) }
                composable(AppPages.ChatbotStart.route) { ChatbotStartScreen(navController) }
                composable(AppPages.Chatbot.route) { ChatbotScreen(navController) }
                composable(AppPages.Notification.route) { NotificationScreen(navController) }
                composable(AppPages.NotificationSettings.route) { NotificationSettingsScreen(navController) }
                composable(AppPages.Settings.route) { SettingsScreen(navController) }
                composable(AppPages.Favourite.route) { FavouriteScreen(navController) }
                composable(AppPages.Saved.route) { SavedScreen(navController) }
                composable(AppPages.Language.route) { LanguageScreen(navController) }
                composable(AppPages.Location.route) { LocationScreen(navController) }
                composable(AppPages.Subscription.route) { SubscriptionScreen(navController) }
                composable(AppPages.ReportTabs.route) { ReportTabs(navController) }
                composable(AppPages.Password.route) { PasswordScreen(navController) }
                composable(AppPages.AboutApplication.route) { AboutApplicationScreen(navController) }
                composable(AppPages.FAQ.route) { FAQScreen(navController) }
                composable("${ AppPages.InRooms.route }/{groupId}", arguments = listOf(navArgument("groupId") {type = NavType.StringType})){ backStack ->
                    val groupId = backStack.arguments?.getString("groupId")
                    InRoomsScreen(navController, groupId!!) }
                composable(AppPages.MyProfileDetails.route) { MyProfileDetailsScreen(navController) }
                composable("${AppPages.OtherUsersProfile.route}/{userId}", arguments = listOf(navArgument("userId") {type = NavType.StringType})){ backStack ->
                    val userId = backStack.arguments?.getString("userId")
                    OtherUsersProfileScreen(navController, userId!!)
                } // Composable
                composable("${AppPages.Messaging.route}/{userId}", arguments = listOf(navArgument("userId") {type = NavType.StringType})){ backStack ->
                    val userId = backStack.arguments?.getString("userId")
                    val user = friendsList.first {it.id == userId}
                    MessagingScreen(navController, user)
                } // Composable
            }
        }
    }
}

