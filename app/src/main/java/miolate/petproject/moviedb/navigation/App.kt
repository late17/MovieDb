package miolate.petproject.moviedb.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import miolate.petproject.moviedb.RootViewModel
import miolate.petproject.moviedb.app.base.DataState
import miolate.petproject.moviedb.ui.theme.MovieDbTheme

@Composable
fun App(viewModel: RootViewModel) {
    val state by viewModel.isAuthenticated.collectAsState()
    val appState = rememberAppState()
    val navController = appState.navController
    val scaffoldState = appState.scaffoldState

    LaunchedEffect(key1 = state) {
        when (state) {
            is DataState.Failure -> navController.navigate(NavRoutes.LOGIN.route)
            is DataState.Loading -> navController.navigate(NavRoutes.LOGIN.route)
            is DataState.Success -> navController.navigate(NavRoutes.HOME.route)
        }
    }

    val currentDestination =
        navController.currentBackStackEntryAsState().value?.destination?.route

    MovieDbTheme {
        Scaffold(
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    BottomBar(
                        currentRoute = currentDestination,
                        navigateToRoute = appState::navigateToBottomBarRoute
                    )
                }
            },
            scaffoldState = scaffoldState,
        ) {
            Surface(
                modifier = Modifier.padding(it)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.LOGIN.route,
                ) {
                    navGraph(navController)
                }
            }
        }
    }
}

fun NavGraphBuilder.navGraph(navController: NavController) {
    composable(NavRoutes.LOGIN.route) {

    }
    composable(NavRoutes.HOME.route) {

    }
    composable(NavRoutes.PROFILE.route) {

    }
}

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
) =
    remember(scaffoldState, navController) {
        AppState(scaffoldState, navController)
    }