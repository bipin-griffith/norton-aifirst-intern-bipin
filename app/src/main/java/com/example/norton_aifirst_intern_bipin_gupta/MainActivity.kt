package com.example.norton_aifirst_intern_bipin_gupta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.norton.scamdetector.ui.MainScreen
import com.norton.scamdetector.ui.theme.NortonColors
import com.norton.scamdetector.ui.theme.NortonTheme
import com.norton.scamdetector.ui.viewmodel.ScamDetectorViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NortonTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = NortonColors.Background
                ) {
                    val viewModel: ScamDetectorViewModel =
                        viewModel(factory = ScamDetectorViewModel.provideFactory(applicationContext))
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}