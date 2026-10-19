package com.example.lawyer.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lawyer.ui.theme.PrimaryWhite

@Composable
fun HomeScreen(
    contentPaddingValues: PaddingValues,
    navController: NavController
) {
    LazyColumn(
        contentPadding = contentPaddingValues,
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryWhite),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Upcoming(navController = navController)
        }
        item {
            SearchAdd(navController = navController)
        }
        item {
            UpcomingCases(navController = navController)
        }
    }
}