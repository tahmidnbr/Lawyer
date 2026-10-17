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
    vm: CaseListVm  = koinViewModel (),
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val cases =  uiState.cases

    LazyColumn(
        contentPadding = contentPaddingValues,
        modifier = Modifier
            .background(PrimaryWhite),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SearchingOpt(navController = navController)
        }

        items(items = cases, key = {it.case.id}){case ->
            FilteredCases(caseHearing = case, navController = navController,)
        }
    }

    if (uiState.isLoading){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PrimaryWhite.copy(.8f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadingIcon()
        }
    }
}

@Composable
fun SearchingOpt(
    navController: NavController
){
    var src  by remember { mutableStateOf("") }
    var all by  remember { mutableStateOf(false) }
    var last by  remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CustomTextField(
                value = src,
                onValueChange = { src = it },
                modifier = Modifier
                    .weight(1f),
                placeholder = "Search",
            )
            Button(
                onClick = {
                    navController.navigate("add"){
                        launchSingleTop = true
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    PrimaryGreen
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                )
            }
        }

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                onClick = { all = !all},
                label = {
                    Text("All")
                },
                selected = all,
                leadingIcon =
                    if (all){
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null
                            )
                        }
                    } else  {
                        null
                    },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PrimaryGreen,
                    selectedLabelColor = PrimaryGold,
                    selectedLeadingIconColor = PrimaryGold
                ),
                shape = RoundedCornerShape(12.dp)
            )


            FilterChip(
                onClick = { last = !last},
                label = {
                    Text("Priority")
                },
                selected = last,
                leadingIcon =
                    if (last){
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null
                            )
                        }
                    } else  {
                        null
                    },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PrimaryGreen,
                    selectedLabelColor = PrimaryGold,
                    selectedLeadingIconColor = PrimaryGold
                ),
                shape = RoundedCornerShape(12.dp)
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
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (isOnCaseScreen){
                        Modifier.weight(0.4f)
                    } else {
                        Modifier.weight(0.25f)
                    }
                )
                 // This assigns 30% width to the cover column
        ) {
            CaseFolderCover(
                caseWithHearings = caseHearing,
                modifier = Modifier.fillMaxWidth(), // Fills the 30% area and maintains 2:3 aspect ratio perfectly
                onClick = {
                    navController.navigate("details/${case.id}"){
                        launchSingleTop = isOnCaseScreen
                    }
                },
                isFeature = false
            )
        }

        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxHeight()
                .then(
                    if (isOnCaseScreen){
                        Modifier.weight(0.6f)
                    } else {
                        Modifier.weight(0.75f)
                    }
                )
                ,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    "Case: ${case.caseNo}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 15.sp,
                    color = PrimaryBlack
                )

                Text(
                    "${case.plaintiff} vs ${case.defendant}",
                    style = MaterialTheme.typography.labelSmall,
                    color = PrimaryBlack.copy(.5f)
                )

                Text(
                    caseHearing.hearings.lastOrNull()?.purposeOrProceeding ?: "Case Hearings",
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = if (isOnCaseScreen)4 else 2
                )

            }

            if (isOnCaseScreen){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier
                            .size(12.dp),
                        tint = PrimaryGreen
                    )

                    Text(
                        "Next hearing: ${dateFormatter.format(Date(caseHearing.hearings.lastOrNull()?.hearingDate ?: 31536000000)) }",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

