package com.example.graduation1.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.graduation1.R
import com.example.graduation1.ui.screens.authentication.OnBoarding1Screen
import com.example.graduation1.ui.screens.authentication.OnBoarding2Screen
import com.example.graduation1.ui.screens.authentication.OnBoarding3Screen
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.darkGray
import com.example.graduation1.ui.theme.gray
import kotlinx.coroutines.launch


@Composable
fun ReportTabs(navController: NavHostController) {

    val pagerState = rememberPagerState(pageCount = {2})
    val coroutineScope = rememberCoroutineScope()
    val purpleColor = Color(204,102,200)
    val blueColor = Color(36,36,214)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "back",
                    modifier = Modifier
                        .size(30.dp)
                )
            }

            Spacer(Modifier.weight(1.5f))
        } // Row

        Text(
            text = stringResource(R.string.Report),
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )

        Spacer(Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, gray )
                        .background(if (pagerState.currentPage == 1) Color.White else gray)
                        .padding(vertical = 8.dp)
                        .clickable {
                            coroutineScope.launch {
                                if (pagerState.currentPage != 0)
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            } },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.Today),
                        color = purpleColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, gray )
                    .background(if (pagerState.currentPage == 0) Color.White else gray)
                    .padding(vertical = 8.dp)
                    .clickable {
                        coroutineScope.launch {
                            if (pagerState.currentPage != 1)
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.ThisMonth),
                    color = purpleColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
        } // Row

        Spacer(Modifier.height(20.dp))

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = true,
            modifier = Modifier.weight(1f)) { page ->
            when(page){
                0 -> TodayReportScreen()
                1 -> ThisMonthReportScreen()
            }
        }
    } // Column
}

@Composable
@Preview(showBackground = true)
fun ReportTabsPreview(){
    Graduation1Theme(dynamicColor = false) {
        val nav = rememberNavController()
        ReportTabs(nav)
    }
}