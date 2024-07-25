package miolate.petproject.moviedb.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import miolate.petproject.moviedb.ui.theme.spacing
import miolate.petproject.moviedb.ui.base.SpacerValue
import miolate.petproject.moviedb.ui.theme.SecondaryBlack


@Composable
fun BottomBar(
    currentRoute: String?,
    navigateToRoute: (String) -> Unit = {},
) {
    BottomNavigation(
        backgroundColor = SecondaryBlack
    ) {
        SpacerValue(spacing.small)
        BottomBarRoutes.getEntries.forEach {
            IconAndName(
                stringResource(id = it.nameResId),
                it.iconResId,
                currentRoute == it.navRoute.route
            ) {
                navigateToRoute(it.navRoute.route)
            }
        }
        SpacerValue(spacing.small)
    }
}

@Composable
private fun IconAndName(
    text: String,
    icon: Int,
    isSelected: Boolean,
    navigateToRoute: () -> Unit,
) {
    val animateColorAsState by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colors.onSurface
        else
            MaterialTheme.colors.onSurface.copy(alpha = 0.5f), label = ""
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .navigationBarsPadding()
            .clickable { navigateToRoute() }
            .padding(top = spacing.extraSmall)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            tint = animateColorAsState
        )
        Text(text = text)
    }
}