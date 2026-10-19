package com.example.lawyer.presentation.screens.cases

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.lawyer.logic.vm.CaseListVm
import com.example.lawyer.presentation.LoadingIcon
import com.example.lawyer.presentation.model.CaseWithHearings
import com.example.lawyer.presentation.screens.home.CaseFolderCover
import com.example.lawyer.presentation.screens.home.CustomTextField
import com.example.lawyer.ui.theme.PrimaryBlack
import com.example.lawyer.ui.theme.PrimaryGold
import com.example.lawyer.ui.theme.PrimaryGreen
import com.example.lawyer.ui.theme.PrimaryWhite
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CaseScreen(
    contentPaddingValues: PaddingValues,
    navController: NavController,
    vm: CaseListVm = koinViewModel(),
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val cases = uiState.cases

    LazyColumn(
        contentPadding = contentPaddingValues,
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryWhite),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            SearchingOpt(navController = navController)
        }

        items(items = cases, key = { it.case.id }) { case ->
            FilteredCases(
                caseHearing = case,
                navController = navController
            )
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PrimaryWhite.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            LoadingIcon()
        }
    }
}

@Composable
fun SearchingOpt(
    navController: NavController
) {
    var src by remember { mutableStateOf("") }
    var all by remember { mutableStateOf(false) }
    var last by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomTextField(
                value = src,
                onValueChange = { src = it },
                modifier = Modifier.weight(1f),
                placeholder = "Search cases..."
            )

            Button(
                onClick = {
                    navController.navigate("add") {
                        launchSingleTop = true
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGold,
                    contentColor = PrimaryBlack
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Case",
                    tint = PrimaryBlack
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                onClick = { all = !all },
                label = {
                    Text(
                        "All",
                        fontWeight = FontWeight.Medium
                    )
                },
                selected = all,
                leadingIcon = if (all) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PrimaryGold,
                    selectedLabelColor = PrimaryBlack,
                    selectedLeadingIconColor = PrimaryBlack,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    labelColor = PrimaryBlack
                ),
                shape = RoundedCornerShape(10.dp)
            )

            FilterChip(
                onClick = { last = !last },
                label = {
                    Text(
                        "Priority",
                        fontWeight = FontWeight.Medium
                    )
                },
                selected = last,
                leadingIcon = if (last) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PrimaryGold,
                    selectedLabelColor = PrimaryBlack,
                    selectedLeadingIconColor = PrimaryBlack,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    labelColor = PrimaryBlack
                ),
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
}

@Composable
fun FilteredCases(
    caseHearing: CaseWithHearings,
    navController: NavController,
    isOnCaseScreen: Boolean = true,
) {
    val case = caseHearing.case
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.weight(if (isOnCaseScreen) 0.35f else 0.25f)
        ) {
            CaseFolderCover(
                caseWithHearings = caseHearing,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate("details/${case.id}") {
                        launchSingleTop = isOnCaseScreen
                    }
                },
                isFeature = false
            )
        }

        Column(
            modifier = Modifier
                .weight(if (isOnCaseScreen) 0.65f else 0.75f)
                .fillMaxHeight()
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Case: ${case.caseNo}",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlack
                )

                Text(
                    text = "${case.plaintiff} vs ${case.defendant}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PrimaryBlack.copy(alpha = 0.6f)
                )

                Text(
                    text = caseHearing.hearings.lastOrNull()?.purposeOrProceeding ?: "Case Hearings",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 13.sp,
                    color = PrimaryBlack.copy(alpha = 0.8f),
                    maxLines = if (isOnCaseScreen) 3 else 2
                )
            }

            if (isOnCaseScreen) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = PrimaryGold
                    )

                    Text(
                        text = "Next hearing: ${
                            dateFormatter.format(
                                Date(caseHearing.hearings.lastOrNull()?.hearingDate ?: 31536000000)
                            )
                        }",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp,
                        color = PrimaryBlack.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

