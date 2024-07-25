package miolate.petproject.moviedb.features.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import miolate.petproject.moviedb.R
import miolate.petproject.moviedb.ui.base.SpacerValue
import miolate.petproject.moviedb.ui.base.SpacerWeight
import miolate.petproject.moviedb.ui.theme.spacing

@Composable
fun ProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(spacing.small)
    ) {
        SpacerWeight()
        Button(onClick = { }, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.log_out))
        }
        SpacerValue(spacing.medium)
    }
}