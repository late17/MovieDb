package miolate.petproject.moviedb.features.home

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
import miolate.petproject.moviedb.features.login.LoginEvents
import miolate.petproject.moviedb.features.login.LoginState
import miolate.petproject.moviedb.features.login.LoginViewModel
import miolate.petproject.moviedb.ui.base.SpacerValue
import miolate.petproject.moviedb.ui.base.SpacerWeight
import miolate.petproject.moviedb.ui.base.collectAsEffect
import miolate.petproject.moviedb.ui.navigation.NavRoutes
import miolate.petproject.moviedb.ui.theme.spacing

@Composable
fun HomeScreen(navController: NavController) {

    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UI(uiState, viewModel::onEvent)
}

@Composable
fun UI(
    uiState: HomeState,
    onEvent: (HomeEvents) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(spacing.small)
    ) {
        SpacerWeight()
        Text(text = "Home Screen")
        SpacerValue(spacing.medium)
    }

}
