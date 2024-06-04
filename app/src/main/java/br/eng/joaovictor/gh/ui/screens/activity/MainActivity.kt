package br.eng.joaovictor.gh.ui.screens.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.eng.joaovictor.gh.ui.theme.GithubTestTheme
import br.eng.joaovictor.gh.ui.screens.repository.ListRepositoryScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubTestTheme {
                ListRepositoryScreen()
            }
        }
    }
}
