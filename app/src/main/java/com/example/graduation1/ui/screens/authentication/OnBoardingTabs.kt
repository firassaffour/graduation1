package com.example.graduation1.ui.screens.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.graduation1.R
import com.example.graduation1.darkMode
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.ui.theme.primaryRed
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch


@Composable
fun OnBoardingTabs(navController: NavHostController){

    val pagerState = rememberPagerState(pageCount = {3})
    val coroutineScope = rememberCoroutineScope()
    val systemUiController = rememberSystemUiController()

    SideEffect {
        when {
            pagerState.currentPage == 0 -> {
                systemUiController.setStatusBarColor(primaryRed)
                systemUiController.setNavigationBarColor(primaryRed)
            }

            pagerState.currentPage >= 1 -> {
                systemUiController.setStatusBarColor(Color.White)
                systemUiController.setNavigationBarColor(Color.White)
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(if (pagerState.currentPage == 0) primaryRed else MaterialTheme.colorScheme.background)) {

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.weight(1f)) { page ->
            when(page){
                0 -> OnBoarding1Screen()
                1 -> OnBoarding2Screen()
                2 -> OnBoarding3Screen(navController)
            }
        }

        Row(
            modifier = Modifier
                .background(if (pagerState.currentPage == 0) primaryRed else MaterialTheme.colorScheme.background)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(Modifier.width(10.dp))

            if (pagerState.currentPage == 1 || pagerState.currentPage == 2){
                IconButton(onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    } },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "back",
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }

            else{
                Spacer(Modifier.size(35.dp))
            }

            Spacer(Modifier.weight(1f))

            repeat(3) { index ->

                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (index <= pagerState.currentPage) darkGray
                            else MaterialTheme.colorScheme.surface
                        )
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {

                }
            }

            Spacer(Modifier.weight(1f))

            if (pagerState.currentPage == 0 || pagerState.currentPage == 1){
                IconButton(onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "back",
                        modifier = Modifier
                            .size(30.dp)
                            .rotate(180f)
                    )
                }
            } // if

            else{
                Spacer(Modifier.size(35.dp))
            }

            Spacer(Modifier.width(10.dp))
        } // Row

        Spacer(Modifier.height(20.dp))
    } // Column
}