package br.eng.joaovictor.gh.ui.screens.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import br.eng.joaovictor.gh.navigation.NavGraph
import br.eng.joaovictor.gh.ui.theme.GithubTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            GithubTestTheme {
                NavGraph(navController = navController, modifier = Modifier)
            }
        }
    }
}
