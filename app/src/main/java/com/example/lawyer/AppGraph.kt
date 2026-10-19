package com.example.lawyer

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.lawyer.presentation.Bottombar
import com.example.lawyer.presentation.CommonTopBar
import com.example.lawyer.presentation.Topbar
import com.example.lawyer.presentation.screens
import com.example.lawyer.presentation.screens.sub.AddCases
import com.example.lawyer.presentation.screens.sub.CaseDetails
import com.example.lawyer.presentation.screens.sub.CaseTop
import com.example.lawyer.presentation.screens.menu.MenuScreen

import com.example.lawyer.presentation.screens.OnBoardScreen
import com.example.lawyer.presentation.screens.SplashScreen
import com.example.lawyer.presentation.screens.WelcomeScreen
import com.example.lawyer.presentation.screens.sub.WriteCase
import com.example.lawyer.presentation.screens.cases.CaseScreen
import com.example.lawyer.presentation.screens.dates.DateScreen
import com.example.lawyer.presentation.screens.home.HomeScreen
import com.example.lawyer.ui.theme.PrimaryWhite
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import org.koin.compose.koinInject

@Composable
fun AppGraph(
    preferences: AppPreferences = koinInject()
) {
    val navController = rememberNavController()

    val isOnboarded by preferences.isOnboarded
        .collectAsStateWithLifecycle(initialValue = null)

    if (isOnboarded == null) {
        SplashScreen()
        return
    }

    NavHost(
        startDestination = if (isOnboarded == true) "screenGraph" else "onBoard",
        navController = navController,
    ){
        composable("onBoard") {
            OnBoardScreen(
                navController = navController
            )
        }

        composable("welcome") {
            WelcomeScreen(
                navController = navController
            )
        }

        composable("screenGraph") {
            ScreenGraph(
                modifier = Modifier
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenGraph(modifier: Modifier) {
    val innerNavController = rememberNavController()

    NavHost(
        navController = innerNavController,
        startDestination = "main_screens",
        modifier = modifier.fillMaxSize(),
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        navigation(
            startDestination = "home",
            route = "main_screens"
        ) {
            composable("home") {
                MainScaffoldWrapper(innerNavController) { bPadding ->
                    HomeScreen(
                        contentPaddingValues = PaddingValues(bottom = bPadding),
                        navController = innerNavController
                    )
                }
            }
            composable("cases") {
                MainScaffoldWrapper(innerNavController) { bPadding ->
                    CaseScreen(
                        contentPaddingValues = PaddingValues(bottom = bPadding),
                        navController = innerNavController
                    )
                }
            }
            composable("dates") {
                MainScaffoldWrapper(innerNavController) { bPadding ->
                    DateScreen(
                        contentPaddingValues = PaddingValues(bottom = bPadding),
                        navController = innerNavController
                    )
                }
            }
            composable("menu") {
                MainScaffoldWrapper(innerNavController) { bPadding ->
                    MenuScreen(
                        contentPaddingValues = PaddingValues(bottom = bPadding),
                    )
                }
            }
        }

        composable("add") {
            Scaffold(
                containerColor = PrimaryWhite,
                topBar = { CommonTopBar(innerNavController) }
            ) { subScreen ->
                AddCases(
                    modifier = Modifier.padding(subScreen),
                    navController = innerNavController
                )
            }
        }

        composable("details/{caseId}") { backStackEntry ->
            val caseId = backStackEntry.arguments?.getString("caseId")?.toLongOrNull()
            Scaffold(
                containerColor = PrimaryWhite,
                topBar = { CommonTopBar(innerNavController) }
            ) { subScreen ->
                if (caseId != null) {
                    CaseDetails(
                        caseId = caseId,
                        modifier = Modifier.padding(subScreen),
                        navController = innerNavController
                    )
                }
            }
        }

        composable("write/{caseId}") { backStackEntry ->
            val caseId = backStackEntry.arguments?.getString("caseId")?.toLongOrNull()
            Scaffold(
                containerColor = PrimaryWhite,
                topBar = {
                    if (caseId != null) {
                        CommonTopBar(
                            innerNavController,
                            content = { CaseTop(navController = innerNavController) }
                        )
                    }
                }
            ) { subScreen ->
                WriteCase(modifier = Modifier.padding(subScreen))
            }
        }
    }
}

@Composable
fun MainScaffoldWrapper(
    innerNavController: NavController,
    content: @Composable (Dp) -> Unit
) {
    val hazeState = remember { HazeState() }

    // Scoped only to this wrapper — doesn't affect ScreenGraph
    val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var lastMainRoute by rememberSaveable { mutableStateOf("home") }
    LaunchedEffect(currentRoute) {
        if (currentRoute in listOf("home", "cases", "dates", "menu")) {
            lastMainRoute = currentRoute!!
        }
    }

    val activeRoute = if (currentRoute in listOf("home", "cases", "dates", "menu")) {
        currentRoute ?: lastMainRoute
    } else {
        lastMainRoute
    }

    Scaffold(
        containerColor = PrimaryWhite,
        topBar = { Topbar() },
        bottomBar = {
            Bottombar(
                hazeState = hazeState,
                selectedIndex = screens.indexOfFirst { it.route == activeRoute }.coerceAtLeast(0),
                onSelect = { i ->
                    val route = screens[i].route
                    innerNavController.navigate(route) {
                        popUpTo(innerNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(hazeState)
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            content(innerPadding.calculateBottomPadding())
        }
    }
}