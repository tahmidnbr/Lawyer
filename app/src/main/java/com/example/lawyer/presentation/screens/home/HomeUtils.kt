package com.example.lawyer.presentation.screens.home


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.lawyer.logic.vm.DateVm
import com.example.lawyer.logic.vm.HomeVm
import com.example.lawyer.presentation.model.CaseWithHearings
import com.example.lawyer.ui.theme.PrimaryBlack
import com.example.lawyer.ui.theme.PrimaryGold
import com.example.lawyer.ui.theme.PrimaryWhite
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue


@Composable
fun Upcoming(
    navController: NavController,
    dateVm: DateVm = koinViewModel()
) {
    val dateState by dateVm.uiState.collectAsStateWithLifecycle()
    val hearings = dateState.allHearings

    val todayHearingCount = remember(hearings) {
        val today = Calendar.getInstance()
        val todayYear = today.get(Calendar.YEAR)
        val todayMonth = today.get(Calendar.MONTH)
        val todayDay = today.get(Calendar.DAY_OF_MONTH)

        hearings.count { hearing ->
            val hearingCalendar = Calendar.getInstance().apply {
                timeInMillis = hearing.hearingDate
            }
            hearingCalendar.get(Calendar.YEAR) == todayYear &&
                    hearingCalendar.get(Calendar.MONTH) == todayMonth &&
                    hearingCalendar.get(Calendar.DAY_OF_MONTH) == todayDay
        }
    }

    val currentTime = remember { Date() }
    val monthFormatter = remember { SimpleDateFormat("LLLL", Locale.getDefault()) }

    val currentMonth = remember(currentTime) { monthFormatter.format(currentTime) }
    val currentDay =
        remember(currentTime) { SimpleDateFormat("dd", Locale.getDefault()).format(currentTime) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f), // Matches SearchAdd card styling
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentMonth.uppercase(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = PrimaryBlack
                    )
                    Text(
                        text = currentDay,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = PrimaryGold
                    )
                }

                Text(
                    text = when {
                        dateState.isLoading -> "CHECKING TODAY'S HEARINGS..."
                        todayHearingCount > 0 -> "$todayHearingCount ${if (todayHearingCount == 1) "HEARING" else "HEARINGS"} TODAY"
                        else -> "ITS A FREE DAY YOUR HONOUR"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = PrimaryBlack.copy(alpha = 0.6f)
                )
            }

            IconButton(
                onClick = {
                    navController.navigate("dates") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    contentDescription = "View Calendar",
                    tint = PrimaryBlack,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun SearchAdd(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f), // Subtle static card background
        tonalElevation = 0.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Priority Cases",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = PrimaryBlack,
                letterSpacing = 0.5.sp
            )

            Button(
                onClick = {
                    navController.navigate("add") {
                        launchSingleTop = true
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGold,
                    contentColor = PrimaryBlack
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Add Case",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = PrimaryBlack
        ),
        singleLine = true,
        modifier = modifier,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, PrimaryBlack.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.labelLarge,
                                color = PrimaryBlack.copy(alpha = 0.4f)
                            )
                        }
                        innerTextField()
                    }

                    if (value.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear text",
                            tint = PrimaryBlack.copy(alpha = 0.5f),
                            modifier = Modifier
                                .clickable(onClick = { onValueChange("") })
                                .size(16.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun CaseFolderCover(
    modifier: Modifier = Modifier,
    color: Color = PrimaryGold,
    isFeature: Boolean = true,
    onClick: () -> Unit,
    caseWithHearings: CaseWithHearings? = null
) {
    val tabFraction = 0.06f
    val customPath = remember { Path() }

    Box(
        modifier = modifier
            .aspectRatio(2f / 3f)
            .drawBehind {
                val w = size.width
                val h = size.height
                val r = w * 0.04f
                val tabW = w * tabFraction
                val bodyRight = w - tabW
                val tabBottom = h * 0.28f

                customPath.reset()
                customPath.apply {
                    moveTo(0f, r)
                    quadraticTo(0f, 0f, r, 0f)
                    lineTo(w - r, 0f)
                    quadraticTo(w, 0f, w, r)
                    lineTo(w, tabBottom - r)
                    quadraticTo(w, tabBottom, w - r, tabBottom)
                    lineTo(bodyRight + r, tabBottom)
                    quadraticTo(bodyRight, tabBottom, bodyRight, tabBottom + r)
                    lineTo(bodyRight, h - r)
                    quadraticTo(bodyRight, h, bodyRight - r, h)
                    lineTo(r, h)
                    quadraticTo(0f, h, 0f, h - r)
                    close()
                }

                drawIntoCanvas { canvas ->
                    val shadowPaint = Paint()
                    val frameworkPaint = shadowPaint.asFrameworkPaint()
                    frameworkPaint.color = Color.Transparent.toArgb()
                    frameworkPaint.setShadowLayer(
                        8.dp.toPx(),
                        0f,
                        4.dp.toPx(),
                        Color.Black.copy(alpha = 0.15f).toArgb()
                    )
                    canvas.drawPath(customPath, shadowPaint)
                }

                drawPath(customPath, color)
            }
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopStart)
        ) {
            Column {
                if (caseWithHearings == null) {
                    Text(
                        text = "00",
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = if (isFeature) 44.sp else 24.sp,
                        color = PrimaryWhite
                    )
                } else {
                    Text(
                        text = formatMonth(caseWithHearings.case.creatingDate),
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryWhite.copy(alpha = 0.8f)
                    )
                    Text(
                        text = formatDay(caseWithHearings.case.creatingDate),
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = if (isFeature) 44.sp else 24.sp,
                        color = PrimaryWhite
                    )
                }
            }
        }

        if (isFeature) {
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.BottomStart)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (caseWithHearings == null) {
                        Text(
                            text = "Add Case",
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 15.sp,
                            color = PrimaryWhite,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Tap to create a new case",
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryWhite.copy(alpha = 0.8f)
                        )
                    } else {
                        Text(
                            text = "Case: ${caseWithHearings.case.caseNo}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 15.sp,
                            color = PrimaryWhite,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "${caseWithHearings.case.plaintiff} vs ${caseWithHearings.case.defendant}",
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryWhite.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UpcomingCases(
    navController: NavController,
    vm: HomeVm = koinViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val cases = uiState.priorityCases
    val initialPage = if (cases.isEmpty()) 0 else 1

    key(cases.size) {
        val pagerState = rememberPagerState(
            initialPage = initialPage,
            pageCount = { cases.size + 1 }
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentPadding = PaddingValues(horizontal = 72.dp),
                pageSpacing = 12.dp
            ) { page ->
                val pageOffset =
                    ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                val scaleFactor = lerp(
                    start = 0.88f,
                    stop = 1.0f,
                    fraction = (1f - pageOffset).coerceIn(0f, 1f)
                )

                if (page == 0) {
                    CaseFolderCover(
                        modifier = Modifier.graphicsLayer {
                            scaleX = scaleFactor
                            scaleY = scaleFactor
                        },
                        color = Color.Gray,
                        onClick = {
                            navController.navigate("add") {
                                launchSingleTop = true
                            }
                        }
                    )
                } else {
                    val caseHearing = cases[page - 1]
                    CaseFolderCover(
                        caseWithHearings = caseHearing,
                        modifier = Modifier.graphicsLayer {
                            scaleX = scaleFactor
                            scaleY = scaleFactor
                        },
                        onClick = {
                            navController.navigate("details/${caseHearing.case.id}") {
                                launchSingleTop = true
                            }
                        },
                        isFeature = true
                    )
                }
            }
        }
    }
}

fun formatMonth(timestamp: Long?): String {
    if (timestamp == null) return ""
    return SimpleDateFormat("MMM", Locale.getDefault()).format(Date(timestamp)).uppercase()
}

fun formatDay(timestamp: Long?): String {
    if (timestamp == null) return ""
    return SimpleDateFormat("dd", Locale.getDefault()).format(Date(timestamp))
}