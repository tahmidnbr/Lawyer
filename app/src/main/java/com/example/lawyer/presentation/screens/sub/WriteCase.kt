package com.example.lawyer.presentation.screens.sub

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.lawyer.logic.vm.HearingVm
import com.example.lawyer.presentation.UiEvent
import com.example.lawyer.ui.theme.DangerRed
import com.example.lawyer.ui.theme.PrimaryBlack
import com.example.lawyer.ui.theme.PrimaryGold
import com.example.lawyer.ui.theme.PrimaryGreen
import com.example.lawyer.ui.theme.PrimaryWhite
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun WriteCase(
    modifier: Modifier = Modifier,
    vm: HearingVm  = koinViewModel ()
){
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val formState by vm.formState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.openAddForm()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            WritingBody(
                description = formState.purposeOrProceeding,
                onDescriptionChange = vm::onProceedingChange,
                proceedingDate = formState.hearingDate,
                onDateChange = vm::onDateChange
            )
        }
    }
}


@Composable
fun CaseTop(
    navController: NavController,
    vm: HearingVm = koinViewModel()  // ProceedingVm already loads caseId from SavedStateHandle
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val formState by vm.formState.collectAsStateWithLifecycle()
    val title = uiState.caseTitle

    LaunchedEffect(Unit) {
        vm.events.collect { event ->
            if (event  is UiEvent.NavigateBack) navController.popBackStack()
        }
    }

    if (title.isEmpty()) return

    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )

        if (formState.purposeOrProceeding.isNotBlank()){
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Save Proceeding",
                modifier = Modifier
                    .size(15.dp)
                    .clickable(
                        onClick = {vm.saveHearing()}
                    )
            )
        }
    }
}

@Composable
fun WritingBody(
    description: String,
    onDescriptionChange: (String) -> Unit,
    proceedingDate:  Long,
    onDateChange: (Long) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val formattedDate = remember(proceedingDate) { dateFormatter.format(Date(proceedingDate)) }


    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier
                    .clickable(
                        onClick = { showDatePicker  = true }
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Next Hearing",
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    "Select Hearing Date: $formattedDate",
                    style = MaterialTheme.typography.labelSmall
                )
            }

            if (proceedingDate <= System.currentTimeMillis()){
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Set Hearing Must",
                    tint = DangerRed,
                    modifier = Modifier.size(15.dp)
                )
            }

        }

        BasicTextField(
            value = description,
            onValueChange = onDescriptionChange,
            textStyle = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .height(160.dp),
            decorationBox = { innerField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (description.isEmpty()) {
                                Text(
                                    "Write ",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = PrimaryBlack.copy(.5f)
                                )
                            }
                            innerField()
                        }
                    }
                }
            }
        )

        if (showDatePicker){
            DatePickerModal(
                initialDate = proceedingDate,
                onDateSelected = { timestampMillis ->
                    timestampMillis?.let { onDateChange(it) }
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

@Composable
fun DatePickerModal(
    initialDate: Long,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(
                    "OK",
                    color = PrimaryWhite
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Cancel",
                    color = PrimaryWhite
                )
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = PrimaryGreen,

        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                headlineContentColor = PrimaryWhite,
                containerColor = PrimaryGreen,
                weekdayContentColor = PrimaryGold,
                dayContentColor = PrimaryWhite,
                todayContentColor = PrimaryWhite,
                todayDateBorderColor = PrimaryWhite,
                selectedDayContentColor = PrimaryWhite,
                selectedDayContainerColor = PrimaryGold,
                yearContentColor = PrimaryWhite,
                selectedYearContentColor = PrimaryWhite,
                selectedYearContainerColor = PrimaryGold,

            ),
        )
    }
}