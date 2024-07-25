package miolate.petproject.moviedb

import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import miolate.petproject.moviedb.ui.navigation.App


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<RootViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplashScreen()
        enableEdgeToEdge()
        setContent {
            App(viewModel)
        }
    }

    private fun initSplashScreen() {
        installSplashScreen().setKeepOnScreenCondition {
            viewModel.keepLockScreen.value
        }
    }
}
