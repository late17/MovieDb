package miolate.petproject.moviedb.ui.navigation

import miolate.petproject.moviedb.R

enum class BottomBarRoutes(
    val nameResId: Int,
    val navRoute: NavRoutes,
    val iconResId: Int,
) {
    HOME(R.string.home, NavRoutes.HOME, R.drawable.baseline_home_filled_24),
    PROFILE(R.string.profile, NavRoutes.PROFILE, R.drawable.baseline_person_24);

    companion object {

        val getEntries = BottomBarRoutes.entries.map { it }
    }
}