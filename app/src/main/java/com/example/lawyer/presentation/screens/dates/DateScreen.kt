package com.example.lawyer.presentation.screens.dates

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.lawyer.logic.vm.CaseListVm
import com.example.lawyer.logic.vm.DateVm
import com.example.lawyer.presentation.screens.cases.FilteredCases
import com.example.lawyer.ui.theme.PrimaryBlack
import com.example.lawyer.ui.theme.PrimaryGold
import com.example.lawyer.ui.theme.PrimaryGreen
import com.example.lawyer.ui.theme.PrimaryWhite
import org.koin.compose.viewmodel.koinViewModel
import java.util.Calendar

// ── Placeholder event model (no backend yet) ─────────────────
data class CalendarEvent(
    val day: Int,
    val hasHearing: Boolean = false,
    val hasFiling: Boolean = false
)

// ── Hardcoded placeholder events ─────────────────────────────
//private val placeholderEvents = listOf(
//    CalendarEvent(1,  hasHearing = true),
//    CalendarEvent(5,  hasFiling  = true),
//    CalendarEvent(6,  hasHearing = true),
//    CalendarEvent(7,  hasHearing = true,  hasFiling = true),
//    CalendarEvent(8,  hasHearing = true),
//    CalendarEvent(12, hasFiling  = true),
//    CalendarEvent(13, hasHearing = true),
//    CalendarEvent(14, hasHearing = true,  hasFiling = true),
//    CalendarEvent(15, hasFiling  = true),
//    CalendarEvent(20, hasHearing = true),
//    CalendarEvent(21, hasFiling  = true),
//    CalendarEvent(23, hasHearing = true),
//)

@Composable
fun DateScreen(
    contentPaddingValues: PaddingValues,
    vm: DateVm = koinViewModel(),
    caseListVm: CaseListVm = koinViewModel(),
    navController: NavController
) {

    //case with hearings
    val caseListState by caseListVm.uiState.collectAsStateWithLifecycle()
    val casesWithHearings = caseListState.cases

    //hearings on selected date
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val hearings = uiState.allHearings

    //create calendar instance
    val calendar = remember { Calendar.getInstance() }

    // Tracked state
    var displayedYear  by remember { mutableIntStateOf(calendar.get(Calendar.YEAR)) }
    var displayedMonth by remember { mutableIntStateOf(calendar.get(Calendar.MONTH)) }
    var selectedDay by rememberSaveable {
        mutableIntStateOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
    }
    var isMonthView    by remember { mutableStateOf(true) }

    val todayYear  = calendar.get(Calendar.YEAR)
    val todayMonth = calendar.get(Calendar.MONTH)
    val todayDay   = calendar.get(Calendar.DAY_OF_MONTH)


    // ── Day-of-week labels ────────────────────────
    val dayLabels = listOf("M","T","W","T","F","S","S")
    Row(modifier = Modifier.fillMaxWidth()) {
        dayLabels.forEachIndexed { index, label ->
            // Indices 4 ("F") and 5 ("S") correspond to Friday and Saturday in this list
            val isWeekend = index == 4 || index == 5
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = if (isWeekend) Color.Red else PrimaryBlack.copy(alpha = 0.4f),
                fontWeight = FontWeight.Medium
            )
        }
    }

    val proceedingDays = remember(hearings,  displayedYear,  displayedMonth){
        vm.hearingDaysFor(hearings, displayedYear, displayedMonth)
    }

    val selectedDayHearings = remember(hearings, displayedYear, displayedMonth, selectedDay) {
        vm.hearingsForDay(hearings,displayedYear,displayedMonth,selectedDay)
    }

    val events = remember(proceedingDays) {
        proceedingDays.map { day -> CalendarEvent(day  = day, hasFiling = true) }
    }

    val monthNames = listOf(
        "January","February","March","April","May","June",
        "July","August","September","October","November","December"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryWhite),
        contentPadding = contentPaddingValues
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(Modifier.height(16.dp))

                // ── Header: Month Year + prev/next ────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${monthNames[displayedMonth]} $displayedYear",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlack,
                        fontSize = 20.sp,
                        letterSpacing = 1.sp
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Prev
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .clickable {
                                    if (displayedMonth == 0) {
                                        displayedMonth = 11
                                        displayedYear--
                                    } else displayedMonth--
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("‹", fontSize = 20.sp, color = PrimaryBlack)
                        }

                        // Next
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .clickable {
                                    if (displayedMonth == 11) {
                                        displayedMonth = 0
                                        displayedYear++
                                    } else displayedMonth++
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("›", fontSize = 20.sp, color = PrimaryBlack)
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // ── Month / Week tabs + legend ────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        TabChip("Month", isMonthView) { isMonthView = true }
                        TabChip("Week",  !isMonthView) { isMonthView = false }
                    }

                    // Legend
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LegendDot(color = PrimaryGold,  label = "Hearings")
                        //LegendDot(color = PrimaryGreen, label = "Filings")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ── Day-of-week labels ────────────────────────
                val dayLabels = listOf("M","T","W","T","F","S","S")
                Row(modifier = Modifier.fillMaxWidth()) {
                    dayLabels.forEach { label ->
                        Text(
                            text = label,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryBlack.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // ── Calendar grid ─────────────────────────────
                if (isMonthView) {
                    MonthGrid(
                        year           = displayedYear,
                        month          = displayedMonth,
                        todayYear      = todayYear,
                        todayMonth     = todayMonth,
                        todayDay       = todayDay,
                        selectedDay    = selectedDay,
                        events         = events,
                        onDaySelected  = { selectedDay = it }
                    )
                } else {
                    WeekGrid(
                        year          = displayedYear,
                        month         = displayedMonth,
                        selectedDay   = selectedDay,
                        todayDay      = todayDay,
                        todayMonth    = todayMonth,
                        todayYear     = todayYear,
                        events        = events,
                        onDaySelected = { selectedDay = it }
                    )
                }

                Spacer(Modifier.height(24.dp))

                // ── Selected day events list ──────────────────
                val dayEvent = events.find { it.day == selectedDay }
                Text(
                    text = "${monthNames[displayedMonth]} $selectedDay",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryBlack
                )
                Spacer(Modifier.height(8.dp))

                if (dayEvent == null || (!dayEvent.hasHearing && !dayEvent.hasFiling)) {
                    Text(
                        "No events scheduled",
                        style = MaterialTheme.typography.bodySmall,
                        color = PrimaryBlack.copy(alpha = 0.4f)
                    )
                } else {
                    selectedDayHearings
                        .mapNotNull { hearing ->
                            casesWithHearings.find { caseWithHearings ->
                                caseWithHearings.case.id == hearing.caseId
                            }
                        }
                        .distinctBy { it.case.id }
                        .forEach { caseWithHearings ->
                            FilteredCases(
                                isOnCaseScreen = false,
                                caseHearing = caseWithHearings,
                                navController = navController
                            )
                        }
                }
            }
        }
    }
}

// ── Month grid ────────────────────────────────────────────────
@Composable
private fun MonthGrid(
    year: Int, month: Int,
    todayYear: Int, todayMonth: Int, todayDay: Int,
    selectedDay: Int,
    events: List<CalendarEvent>,
    onDaySelected: (Int) -> Unit
) {
    val cal = Calendar.getInstance().apply { set(year, month, 1) }
    // Proper 0-indexed Monday start (Monday = 0, ..., Friday = 4, Saturday = 5, Sunday = 6)
    val firstDow = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

    val prevCal = Calendar.getInstance().apply { set(year, month, 1) }.also { it.add(Calendar.MONTH, -1) }
    val daysInPrevMonth = prevCal.getActualMaximum(Calendar.DAY_OF_MONTH)

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        for (row in 0 until 6) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0..6) {
                    val cellIndex = row * 7 + col
                    val offset = cellIndex - firstDow

                    val (day, isCurrentMonth) = when {
                        offset < 0 -> Pair(daysInPrevMonth + offset + 1, false)
                        offset >= daysInMonth -> Pair(offset - daysInMonth + 1, false)
                        else -> Pair(offset + 1, true)
                    }

                    // Friday = 4, Saturday = 5
                    val isWeekend = col == 4 || col == 5

                    val event = if (isCurrentMonth) events.find { it.day == day } else null
                    val isToday = isCurrentMonth && day == todayDay && month == todayMonth && year == todayYear
                    val isSelected = isCurrentMonth && day == selectedDay

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        DayCell(
                            day = day,
                            isToday = isToday,
                            isSelected = isSelected,
                            isCurrentMonth = isCurrentMonth,
                            isWeekend = isWeekend,
                            hasHearing = event?.hasHearing == true,
                            hasFiling = event?.hasFiling == true,
                            onClick = { if (isCurrentMonth) onDaySelected(day) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekGrid(
    year: Int, month: Int,
    selectedDay: Int,
    todayDay: Int, todayMonth: Int, todayYear: Int,
    events: List<CalendarEvent>,
    onDaySelected: (Int) -> Unit
) {
    val cal = Calendar.getInstance().apply { set(year, month, selectedDay) }
    // Proper 0-indexed Monday start
    val dow = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7
    val weekStart = selectedDay - dow
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

    Row(modifier = Modifier.fillMaxWidth()) {
        for (col in 0..6) {
            val day = weekStart + col
            val isValid = day in 1..daysInMonth
            val event   = if (isValid) events.find { it.day == day } else null
            val isToday = isValid && day == todayDay &&
                    month == todayMonth && year == todayYear
            val isSelected = isValid && day == selectedDay

            // Friday = 4, Saturday = 5
            val isWeekend = col == 4 || col == 5

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                if (isValid) {
                    DayCell(
                        day        = day,
                        isToday    = isToday,
                        isSelected = isSelected,
                        isCurrentMonth = true,
                        isWeekend  = isWeekend,
                        hasHearing = event?.hasHearing == true,
                        hasFiling  = event?.hasFiling  == true,
                        onClick    = { onDaySelected(day) }
                    )
                }
            }
        }
    }
}

// ── Single day cell ───────────────────────────────────────────
@Composable
private fun DayCell(
    day: Int,
    isToday: Boolean,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    isWeekend: Boolean,                     // ← Added parameter
    hasHearing: Boolean,
    hasFiling: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(2.dp)
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .then(
                when {
                    isSelected -> Modifier.background(PrimaryBlack)
                    isToday    -> Modifier.border(1.5.dp, PrimaryGold, RoundedCornerShape(10.dp))
                    else       -> Modifier
                }
            )
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = day.toString(),
            fontSize = 13.sp,
            fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
            color = when {
                isSelected      -> PrimaryWhite
                isToday         -> PrimaryGold
                !isCurrentMonth -> PrimaryBlack.copy(alpha = 0.2f)
                isWeekend       -> Color.Red             // ← Weekend color (Off-day in Bangladesh)
                else            -> PrimaryBlack
            }
        )

        // Only show dots for current month days
        if (isCurrentMonth && (hasHearing || hasFiling)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.padding(top = 2.dp)
            ) {
                if (hasHearing) EventDot(if (isSelected) PrimaryWhite else PrimaryGold)
                if (hasFiling)  EventDot(if (isSelected) PrimaryWhite.copy(.7f) else PrimaryGreen)
            }
        }
    }
}

// ── Small dot under date ──────────────────────────────────────
@Composable
private fun EventDot(color: Color) {
    Box(
        modifier = Modifier
            .size(4.dp)
            .clip(CircleShape)
            .background(color)
    )
}

// ── Tab chip ─────────────────────────────────────────────────
@Composable
private fun TabChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) PrimaryBlack else Color.Transparent)
            .border(
                width = 1.dp,
                color = if (selected) PrimaryBlack else PrimaryBlack.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (selected) PrimaryWhite else PrimaryBlack.copy(alpha = 0.5f)
        )
    }
}

// ── Legend dot + label ────────────────────────────────────────
@Composable
private fun LegendDot(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = PrimaryBlack.copy(alpha = 0.5f)
        )
    }
}

// ── Event row in day detail ───────────────────────────────────
@Composable
private fun EventRow(dot: Color, label: String, note: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(dot)
        )
        Column {
            Text(label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = PrimaryBlack)
            Text(note,  fontSize = 11.sp, color = PrimaryBlack.copy(alpha = 0.5f))
        }
    }
}
