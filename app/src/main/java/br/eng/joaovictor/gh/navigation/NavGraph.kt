package br.eng.joaovictor.gh.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.eng.joaovictor.gh.ui.screens.repositories.ListRepositoryScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(navController = navController, startDestination = Screen.Repositories.route, modifier = modifier) {
        composable(Screen.Repositories.route){
            ListRepositoryScreen()
        }
    }
}