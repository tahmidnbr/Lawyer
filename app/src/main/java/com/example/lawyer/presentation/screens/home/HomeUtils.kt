package com.example.lawyer.presentation.screens.home


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.lawyer.ui.theme.PrimaryGreen
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
){
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
    val dayFormatter = remember { SimpleDateFormat("dd", Locale.getDefault()) }

    val currentMonth  = remember(currentTime) { monthFormatter.format(currentTime) }
    val currentDay  = remember(currentTime) { dayFormatter.format(currentTime) }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
        ) {
            Row(
                modifier = Modifier
                    .padding( 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
            ) {
                Text(
                    currentMonth.uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlack
                )
                Text(
                    currentDay,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGold
                )
            }

            Text(
                text = when{
                    dateState.isLoading -> "CHECKING TODAY'S HEARINGS..."
                    todayHearingCount > 0 ->
                        "$todayHearingCount ${
                            if (todayHearingCount == 1) "HEARING" else "HEARINGS"
                        } TODAY"
                    else -> "Its a free day your Honour".uppercase()

                },
                style = MaterialTheme.typography.bodySmall,
                color = PrimaryGreen.copy(alpha = .65f)
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowForwardIos,
                modifier = Modifier
                    .clickable(onClick = {
                        navController.navigate("dates"){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }),
                contentDescription = null,
                tint = PrimaryBlack
            )
        }
    }
}

@Composable
fun SearchAdd(navController: NavController) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(horizontal = 12.dp)
    ) {
        Text(
            "Priority Cases",
            modifier = Modifier
                .weight(1f),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            letterSpacing = 1.sp
        )

        TextButton(
            onClick = {
                navController.navigate("add")
            },
        ) {
            Text(
                "Add",
                color = PrimaryGold,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun CustomTextField(
    value : String,
    onValueChange: (String) -> Unit,
    modifier : Modifier = Modifier,
    placeholder: String
){
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.labelLarge,
        singleLine = true,
        modifier = modifier,

        decorationBox = { innerFiled ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, PrimaryBlack.copy(.05f), RoundedCornerShape(12.dp))
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ){
                        if (value.isEmpty()){
                            Text(
                                placeholder,
                                style = MaterialTheme.typography.labelLarge,
                                color = PrimaryBlack.copy(.5f)
                            )
                        }

                        innerFiled()
                    }

                    if (value.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear, // Changed to Clear since it only shows when text is present
                            contentDescription = "Clear text",
                            tint = PrimaryBlack.copy(.5f),
                            modifier = Modifier
                                .clickable(onClick = { onValueChange("")})
                                .size(15.dp)
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
    caseWithHearings: CaseWithHearings ? = null
) {
    val tabFraction = 0.06f   // tab width as a fraction of total width
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

                // 2. Clear previous coordinates and rewrite them on the SAME object
                customPath.reset()
                customPath.apply {
                    moveTo(0f, r)
                    quadraticBezierTo(0f, 0f, r, 0f)
                    lineTo(w - r, 0f)
                    quadraticBezierTo(w, 0f, w, r)
                    lineTo(w, tabBottom - r)
                    quadraticBezierTo(w, tabBottom, w - r, tabBottom)
                    lineTo(bodyRight + r, tabBottom)
                    quadraticBezierTo(bodyRight, tabBottom, bodyRight, tabBottom + r)
                    lineTo(bodyRight, h - r)
                    quadraticBezierTo(bodyRight, h, bodyRight - r, h)
                    lineTo(r, h)
                    quadraticBezierTo(0f, h, 0f, h - r)
                    close()
                }

                // 3. Draw shadow
                drawIntoCanvas { canvas ->
                    val shadowPaint = Paint()
                    val frameworkPaint = shadowPaint.asFrameworkPaint()
                    frameworkPaint.color = Color.Transparent.toArgb()
                    frameworkPaint.setShadowLayer(
                        8.dp.toPx(),
                        0f,
                        4.dp.toPx(),
                        Color.Black.copy(alpha = 0.2f).toArgb()
                    )
                    canvas.drawPath(customPath, shadowPaint)
                }

                // 4. Draw color
                drawPath(customPath, color)
            }
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopStart)
        ) {
            Column(
                //verticalArrangement = Arrangement.Center,
                //horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isFeature){
                    if (caseWithHearings == null){
                        Text(
                            "00",
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 24.sp,
                            color = PrimaryWhite
                        )
                    } else{
                        Text(
                            formatMonth(caseWithHearings?.case?.creatingDate),
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryWhite
                        )
                        Text(
                            formatDay(caseWithHearings?.case?.creatingDate),
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 24.sp,
                            color = PrimaryWhite
                        )
                    }
                } else {
                    if (caseWithHearings == null){
                        Text(
                            "00",
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 44.sp,
                            color = PrimaryWhite
                        )
                    } else{
                        Text(
                            formatMonth(caseWithHearings.case.creatingDate),
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryWhite
                        )
                        Text(
                            formatDay(caseWithHearings.case.creatingDate),
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 44.sp,
                            color = PrimaryWhite
                        )
                    }
                }
            }
        }

        if (isFeature){
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .align(alignment = Alignment.BottomStart)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    //horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (caseWithHearings == null){
                        Text(
                            "Add Case",
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 15.sp,
                            color = PrimaryWhite,
                            letterSpacing = 1.sp
                        )

                        Text(
                            "Click this folder to add a new case",
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryWhite
                        )
                    } else {
                        Text(
                            "Case: ${caseWithHearings?.case?.caseNo ?: ""}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 15.sp,
                            color = PrimaryWhite,
                            letterSpacing = 1.sp

                        )

                        Text(
                            "${caseWithHearings?.case?.plaintiff} vs ${caseWithHearings?.case?.defendant}",
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryWhite
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
){
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
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 72.dp),
                pageSpacing = 6.dp
            ) { page ->

                val pageOffset =
                    ((pagerState.currentPage - page) +
                            pagerState.currentPageOffsetFraction)
                        .absoluteValue

                val scaleFactor = lerp(
                    start = 0.88f,
                    stop = 1.0f,
                    fraction = (1f - pageOffset.coerceIn(0f, 1f))
                )

                if (page == 0) {

                    CaseFolderCover(
                        modifier = Modifier.graphicsLayer {
                            scaleX = scaleFactor
                            scaleY = scaleFactor
                        },
                        color = Color.Gray,
                        onClick = {
                            navController.navigate("add")
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
                            navController.navigate("details/${caseHearing.case.id}")
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

    return SimpleDateFormat(
        "MMM",
        Locale.getDefault()
    ).format(Date(timestamp)).uppercase()
}

fun formatDay(timestamp: Long?): String {
    if (timestamp == null) return ""

    return SimpleDateFormat(
        "dd",
        Locale.getDefault()
    ).format(Date(timestamp))
}