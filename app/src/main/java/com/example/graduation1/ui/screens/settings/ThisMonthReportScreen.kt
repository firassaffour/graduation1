package com.example.graduation1.ui.screens.settings

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.graduation1.R
import com.example.graduation1.ui.theme.Graduation1Theme
import com.example.graduation1.ui.theme.gray
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.entryModelOf


@Composable
fun ThisMonthReportScreen() {

    val progress by remember { mutableFloatStateOf(0.3f) }
    val purpleColor = Color(204,102,200)
    val blueColor = Color(36,36,214)

    val weeks = listOf("week1", "week2", "week3", "week4", "week5")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.weight(1f))

        Column(modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, gray, RoundedCornerShape(8.dp))
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = stringResource(R.string.MonthlyProgress),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = blueColor,
                modifier = Modifier
                    .align(Alignment.Start)
            )

            Spacer(Modifier.height(20.dp))

            Chart(
                chart = columnChart(
                    columns = listOf(
                        LineComponent(purpleColor.toArgb(), 5f),
                        LineComponent(blueColor.toArgb(), 5f),
                        LineComponent(Color.Red.toArgb(), 5f),
                    ),
                ),
                model = entryModelOf(4f, 8f, 2f, 6f, 7f),
                startAxis = rememberStartAxis(
                    label = textComponent{
                        color = purpleColor.toArgb()
                        textSizeSp = 12f
                    },
                    valueFormatter = {value, _ ->
                        "${value.toInt() * 10}%"
                    }
                ),
                bottomAxis = rememberBottomAxis(
                    valueFormatter = {value, _ ->
                        weeks.getOrNull(value.toInt()) ?: ""
                    }
                )
            )
        }

        Spacer(Modifier.weight(1f))


        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Box(contentAlignment = Alignment.Center) {

                Canvas(modifier = Modifier.size(150.dp)) {

                    drawArc(
                        color = purpleColor,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 30f)
                    )

                    drawArc(
                        color = blueColor,
                        startAngle = -90f,
                        sweepAngle = 360 * progress,
                        useCenter = false,
                        style = Stroke(width = 30f)
                    )
                }

                Text("${(progress * 100).toInt()}%",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(Modifier.weight(1f))

                Box(modifier = Modifier
                    .clip(CircleShape)
                    .background(blueColor)
                    .size(15.dp))

                Spacer(Modifier.width(10.dp))

                Text(
                    text = stringResource(R.string.Done),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                Box(modifier = Modifier
                    .clip(CircleShape)
                    .background(purpleColor)
                    .size(15.dp))

                Spacer(Modifier.width(10.dp))

                Text(
                    text = stringResource(R.string.Progress),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))
            } // Row
        }

        Spacer(Modifier.weight(1f))
    } // Column
}

@Composable
@Preview(showBackground = true)
fun ThisMonthReportScreenPreview(){
    Graduation1Theme(dynamicColor = false) {
        ThisMonthReportScreen()
    }
}