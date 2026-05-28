package com.example.graduation1

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.graduation1.data.remote.RetrofitInstance
import com.example.graduation1.data.repository.AppModule
import com.example.graduation1.data.repository.AuthRepository
import com.example.graduation1.data.repository.ChatRepository
import com.example.graduation1.data.repository.GroupsRepository
import com.example.graduation1.data.repository.PostRepository
import com.example.graduation1.data.repository.UserRepository
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
import com.example.graduation1.ui.screens.groups.GroupDetailsScreen
import com.example.graduation1.ui.screens.groups.GroupsListScreen
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
import com.example.graduation1.viewmodel.AuthViewModel
import com.example.graduation1.viewmodel.AuthViewModelFactory
import com.example.graduation1.viewmodel.ChatViewModel
import com.example.graduation1.viewmodel.ChatViewModelFactory
import com.example.graduation1.viewmodel.ChatbotViewModel
import com.example.graduation1.viewmodel.ChatbotViewModelFactory
import com.example.graduation1.viewmodel.GroupsViewModel
import com.example.graduation1.viewmodel.GroupsViewModelFactory
import com.example.graduation1.viewmodel.NotificationViewModel
import com.example.graduation1.viewmodel.NotificationViewModelFactory
import com.example.graduation1.viewmodel.PostViewModel
import com.example.graduation1.viewmodel.PostViewModelFactory
import com.example.graduation1.viewmodel.UserViewModel
import com.example.graduation1.viewmodel.UserViewModelFactory
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

        val postViewModel : PostViewModel = viewModel(
            factory = PostViewModelFactory(AppModule.postRepository, AppModule.userRepository)
        )

        val chatViewModel : ChatViewModel = viewModel(
            factory = ChatViewModelFactory(AppModule.chatRepository, AppModule.userRepository)
        )

        val userViewModel : UserViewModel = viewModel(
            factory = UserViewModelFactory(AppModule.userRepository)
        )

        val groupsViewModel : GroupsViewModel = viewModel(
            factory = GroupsViewModelFactory(AppModule.groupsRepository, AppModule.userRepository)
        )

        val authViewModel : AuthViewModel = viewModel(
            factory = AuthViewModelFactory(AppModule.authRepository)
        )

        val notificationViewModel : NotificationViewModel = viewModel(
            factory = NotificationViewModelFactory(AppModule.notificationRepository)
        )

        val chatbotViewModel : ChatbotViewModel = viewModel(
            factory = ChatbotViewModelFactory(AppModule.chatbotRepository, AppModule.userRepository)
        )

        val showBottomBar = currentRoute in BottomNavItem.items.map { it.route }


        Scaffold(
            floatingActionButton = {
                if (showBottomBar) {
                    FloatingActionButton(
                        onClick = {navController.navigate(AppPages.CreatePost.route)},
                        containerColor = primaryRed,
                        shape = CircleShape,
                        elevation = FloatingActionButtonDefaults.elevation(8.dp),
                        modifier = Modifier
                            .size(40.dp)
                            .offset(y = 70.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.create),
                            contentDescription = "create",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,

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
                composable(BottomNavItem.Home.route) { HomeScreen(navController, postViewModel, userViewModel, notificationViewModel) }
                composable(BottomNavItem.Chat.route) { ChatTabs(navController, chatViewModel, groupsViewModel, userViewModel) }
                composable(AppPages.MyRooms.route) { MyRoomsScreen(navController, groupsViewModel, userViewModel) }
                composable(AppPages.CreatePost.route) { CreatePostScreen(navController, userViewModel, postViewModel, groupsViewModel) }
                composable(BottomNavItem.Friends.route) { FriendsScreen(navController, userViewModel) }
                composable(AppPages.SignUp.route) { SignUpScreen(navController, authViewModel) }
                composable(AppPages.Login.route) { LoginScreen(navController, authViewModel) }
                composable(AppPages.CreateAccount.route) { CreateAccountScreen(navController) }
                composable("${AppPages.GroupsList.route}/{userId}", arguments = listOf(navArgument("userId") {type = NavType.StringType})){ backStack ->
                    val userId = backStack.arguments?.getString("userId")
                    GroupsListScreen(navController, userId!!, groupsViewModel, userViewModel) {} }
                composable("${AppPages.Post.route}/{postId}", arguments = listOf(navArgument("postId") {type = NavType.StringType})){ backStack ->
                val postId = backStack.arguments?.getString("postId")
                    PostScreen(navController, postId!!, postViewModel, userViewModel)
                } // Composable
                composable(BottomNavItem.Profile.route) { MyProfileScreen(navController) }
                composable(AppPages.EditProfile.route) { EditProfileScreen(navController) }
                composable(AppPages.ChatbotStart.route) { ChatbotStartScreen(navController) }
                composable(AppPages.Chatbot.route) { ChatbotScreen(navController, chatbotViewModel) }
                composable(AppPages.Notification.route) { NotificationScreen(navController, notificationViewModel) }
                composable(AppPages.NotificationSettings.route) { NotificationSettingsScreen(navController) }
                composable(AppPages.Settings.route) { SettingsScreen(navController) }
                composable(AppPages.Favourite.route) { FavouriteScreen(navController, postViewModel, userViewModel) }
                composable(AppPages.Saved.route) { SavedScreen(navController, postViewModel, userViewModel) }
                composable(AppPages.Language.route) { LanguageScreen(navController) }
                composable(AppPages.Location.route) { LocationScreen(navController) }
                composable(AppPages.Subscription.route) { SubscriptionScreen(navController) }
                composable(AppPages.ReportTabs.route) { ReportTabs(navController) }
                composable(AppPages.Password.route) { PasswordScreen(navController) }
                composable(AppPages.AboutApplication.route) { AboutApplicationScreen(navController) }
                composable(AppPages.FAQ.route) { FAQScreen(navController) }
                composable("${ AppPages.InRooms.route }/{groupId}", arguments = listOf(navArgument("groupId") {type = NavType.StringType})){ backStack ->
                    val groupId = backStack.arguments?.getString("groupId")
                    InRoomsScreen(navController, groupId!!, groupsViewModel, userViewModel) }
                composable(AppPages.MyProfileDetails.route) { MyProfileDetailsScreen(navController, userViewModel, postViewModel, groupsViewModel) }
                composable("${AppPages.OtherUsersProfile.route}/{userId}", arguments = listOf(navArgument("userId") {type = NavType.StringType})){ backStack ->
                    val userId = backStack.arguments?.getString("userId")
                    OtherUsersProfileScreen(navController, userId!!, userViewModel, postViewModel, groupsViewModel)
                } // Composable
                composable("${AppPages.Messaging.route}/{chatId}", arguments = listOf(navArgument("chatId") {type = NavType.StringType})){ backStack ->
                    val chatId = backStack.arguments?.getString("chatId")
                    MessagingScreen(navController, chatId!!, chatViewModel, userViewModel)
                } // Composable
                composable("${AppPages.GroupDetails.route}/{groupId}", arguments = listOf(navArgument("groupId") {type = NavType.StringType})){ backStack ->
                    val groupId = backStack.arguments?.getString("groupId")
                    GroupDetailsScreen(navController, groupId!!,groupsViewModel, postViewModel, userViewModel)
                } // Composable
            }
        }
    }
}

