package com.example.graduation1

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.graduation1.domain.models.ChatItem
import com.example.graduation1.domain.models.Comment
import com.example.graduation1.domain.models.Group
import com.example.graduation1.domain.models.Message
import com.example.graduation1.domain.models.Notification
import com.example.graduation1.domain.models.PostData
import com.example.graduation1.domain.models.User

val ferasImage = R.drawable.ferasai
val saraImage = R.drawable.saraai
val omerImage = R.drawable.omerai
val ahmedImage = R.drawable.ahmedai
val seifImage = R.drawable.seifai
val mahmodImage = R.drawable.mahmodai

val followers1 = listOf("1", "2")
val followers2 = listOf("4", "3", "1")
val followers3 = listOf("1", "3", "5", "6")

val user = User("2", "Feras", ferasImage, "feras@gmail.com", "Android Developer", "Syria", emptyList(), followersList =  followers1, followingList =  followers3, groupsList = listOf("1", "3", "4"), gender = "Male", birthday =  "June, 04, 2004", isOnline =  true)
val aiImage = "https://previews.123rf.com/images/rokastenys/rokastenys2212/rokastenys221200010/196063269-ai-conceptual-image-artificial-intelligence-ai-is-intelligence%C3%A2%E2%82%AC%E2%80%9Dperceiving-synthesizing-and.jpg"
val uiImage = "https://previews.123rf.com/images/tirachard/tirachard2007/tirachard200700034/153631619-asian-businessmen-and-businesswomen-meeting-brainstorming-ideas-about-creative-web-design-planning.jpg"
val backendImage = "https://previews.123rf.com/images/khan05/khan052603/khan05260308350/299809123-online-store-dropshipping-concept-on-computer-screen.jpg"
val javaImage = "https://previews.123rf.com/images/grispb/grispb2011/grispb201100088/158250389-the-concept-of-software-development-java-programming-language-java-logo-on-a-blue-pcb-background.jpg"
var darkMode by mutableStateOf(false)
var language by mutableStateOf("en")
var selectedPostPage by mutableStateOf<PostData?>(null)
val codeSnippet = """
    fun main() {
      printIn("Hello World")
    }
""".trimIndent()
val codeSnippet1 = """
    function add(a, b) {
     return a + b;
    }
    console.log(add(5, 3)) // Output: 8
""".trimIndent()



val messageList = listOf(
    Message("1", "hey there", "2", "2:10"),
    Message("2", "hey, i'm navigator, what should  call you ?", "AI", "2:11"),
    Message("3", "Feras", "2", "2:11"),
    Message("4", "nice to meet you Feras", "AI", "2:12"),
)

val messageList2 = listOf(
    Message("1", "hey there", "2", "2:10", isSeen = true),
    Message("2", "hey how you doing ?", "AI", "2:11"),
    Message("3", "fine, and you", "2", "2:11", isSeen = true),
    Message("4", "fine thank you", "AI", "2:12"),
    Message("5", "look at this image i found on internet", "AI", "2:13", javaImage),
)

val chatList = listOf(
    ChatItem("1", "Sara", saraImage, "how are you doing", "9:28"),
    ChatItem("2", "Feras", ferasImage, "how are you doing", "9:28", true, 2),
    ChatItem("3", "Mahmod", mahmodImage, "how are you doing", "9:28", unSeenMessagesCount = 3),
    ChatItem("4", "Ahmed", ahmedImage, "how are you doing", "9:28"),
    ChatItem("5", "Omer", omerImage, "how are you doing", "9:28"),
    ChatItem("6", "Seif", seifImage, "how are you doing", "9:28")
)

val friendsList = listOf(
    User("1", "Sara", saraImage, "sara@gmail.com", "UI/UX", "Egypt", followersList =   followers1, followingList =  followers2, groupsList = listOf("2","3"), gender =  "Female", birthday = "June, 17, 2004"),
    User("2", "Feras", ferasImage, "feras@gmail.com", "Android Developer", "Syria", followersList =  followers2, followingList =  followers3, groupsList = listOf("2","3","1"), gender = "Male", birthday ="June, 4, 2004", isOnline = true),
    User("3", "Mahmod", mahmodImage, "mohammed@gmail.com", "Backend Engineer", "Egypt", followersList =  followers3, followingList =  followers1, groupsList = listOf("2","1"), gender =  "Male", birthday =  "June, 17, 2004"),
    User("4", "Ahmed", ahmedImage, "ahmed@gmail.com", "Backend Engineer", "Egypt", followersList =  followers2, followingList =  followers3, groupsList = listOf("5","3"), gender =  "Male", birthday =  "June, 17, 2004"),
    User("5", "Omer", omerImage, "omer@gmail.com", "AI Trainer", "Egypt", followersList =  followers1, followingList =  followers3, groupsList = listOf("2","1","5"), gender =  "Male", birthday =  "June, 17, 2004"),
    User("6", "Seif", seifImage, "seif@gmail.com", "Backend Engineer", "Egypt", followersList =  followers2, followingList =  followers1, groupsList = listOf("2","3","4"), gender =  "Male", birthday =  "June, 17, 2004"),
)

val commentsList = listOf(
    Comment("1", "Sara", saraImage, "how are you doing", "2:11",false, 20),
    Comment("2", "Feras", ferasImage, "i don't agree with you because this is not a good thing to say on internet, we should talk in private, i will text you there", "5:21", false, 2),
    Comment("3", "Mahmod", mahmodImage, "i support this", "2:22", false, 1),
    Comment("4", "Ahmed", ahmedImage, "amazing", "2:12", false),
    Comment("5", "Omer", omerImage, "good", "9:11", false, 10),
    Comment("6", "Seif", seifImage, "i think this is a good idea", "2:33", false, 3)
)

var postList = listOf(
    PostData("1","1","2", "Java Bros", javaImage,"Feras", "","this is where every java developer started \uD83D\uDE02", codeSnippet = codeSnippet, likesCount = 20, commentsList = commentsList, postDate = "2:15 PM"),
    PostData("2","2","6", "AI Professionals", aiImage,"Seif","", "AI is changing the world and every programmer on this earth want to learn about it, here you will find all you need and want", likesCount = 113, commentsList = commentsList, commentsCount = commentsList.size, postDate = "6:30 PM"),
    PostData("3","3","1", "UI/UX", uiImage,"Sara","", "one of the biggest UI/UX community in the whole programming world", uiImage, likesCount = 10, commentsList = commentsList, commentsCount = commentsList.size, postDate = "10:23 AM"),
    PostData("4","4","4", "Backend Teams", backendImage,"Ahmed","", "can anyone fix this code ? ", codeSnippet = codeSnippet1, likesCount = 31, commentsList = commentsList, commentsCount = commentsList.size, postDate = "1:43 AM"),
    PostData("5","5","2", "Java Bros", javaImage,"Feras", "","i advice new developers to learn java before kotlin so they understand how old programming languages works", likesCount = 204, commentsList = commentsList, postDate = "2 days")
)

val groupsList = listOf(
    Group("1","AI", aiImage, 20, friendsList, 4),
    Group("2","UX meet up", uiImage, 23, friendsList, 2),
    Group("3","Java",  javaImage, 30, friendsList, 0),
    Group("4","Backend", backendImage, 40, friendsList, 1),
    Group("5","AI", aiImage, 3, friendsList, 5)
)

val todayNotificationList = listOf(
    Notification("1", "Website Developers", aiImage, "2:11"),
    Notification("2", "Android Developers", uiImage, "3:32"),
    Notification("3", "Backend Developers", backendImage, "11:23"),
)

val lastWeekNotificationList = listOf(
    Notification("1", "Website Developers", aiImage, "2:11"),
    Notification("2", "Android Developers", uiImage, "3:32"),
    Notification("3", "Backend Developers", backendImage, "11:23"),
)

val favouritePost = listOf(postList[0], postList[1])

val savedPost = listOf(postList[0], postList[1], postList[2])