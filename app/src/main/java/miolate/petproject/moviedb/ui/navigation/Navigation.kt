package miolate.petproject.moviedb.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

fun NavController.navigate(
    route: NavRoutes,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    this.navigate(
        route.route,
        navOptions,
        navigatorExtras
    )
}

fun NavController.navigateAndRemoveFromBackStack(
    currentRoute: NavRoutes,
    route: NavRoutes,
){
    val navOptions = NavOptions.Builder()
        .setPopUpTo(currentRoute.route, inclusive = true)
        .setLaunchSingleTop(true)
        .build()
    this.navigate(
        route.route,
        navOptions
    )
}