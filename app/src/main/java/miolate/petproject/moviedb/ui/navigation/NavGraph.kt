package miolate.petproject.moviedb.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import miolate.petproject.moviedb.features.home.HomeScreen
import miolate.petproject.moviedb.features.login.LoginScreen
import miolate.petproject.moviedb.features.profile.ProfileScreen

fun NavGraphBuilder.navGraph(navController: NavController) {
    composable(NavRoutes.DEFAULT.route){

    }
    composable(NavRoutes.LOGIN.route) {
        LoginScreen(navController)
    }
    composable(NavRoutes.HOME.route) {
        HomeScreen(navController)
    }
    composable(NavRoutes.PROFILE.route) {
        ProfileScreen(navController)
    }
}