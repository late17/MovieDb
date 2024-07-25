package miolate.petproject.moviedb.features.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import miolate.petproject.moviedb.R
import miolate.petproject.moviedb.ui.base.SpacerValue
import miolate.petproject.moviedb.ui.base.SpacerWeight
import miolate.petproject.moviedb.ui.base.collectAsEffect
import miolate.petproject.moviedb.ui.navigation.NavRoutes
import miolate.petproject.moviedb.ui.theme.spacing

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: LoginViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.navigation.collectAsEffect {
        when (it) {
            LoginViewModel.Navigation.NavigateToHome -> {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(NavRoutes.LOGIN.route, inclusive = true)
                    .setLaunchSingleTop(true)
                    .build()
                navController.navigate(
                    NavRoutes.HOME.route,
                    navOptions
                )
            }

            else -> {}
        }
    }

    UI(uiState, viewModel::onEvent)
}

@Composable
fun UI(
    uiState: LoginState,
    onEvent: (LoginEvents) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(spacing.small)
    ) {
        SpacerWeight()
        Button(onClick = { onEvent(LoginEvents.SignIn) }, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.login_with_google))
        }
        SpacerValue(spacing.medium)
    }

}

