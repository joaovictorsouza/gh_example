package br.eng.joaovictor.gh.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.eng.joaovictor.gh.data.model.Repo
import br.eng.joaovictor.gh.ui.screens.pull_requests.PullRequestsScreen
import br.eng.joaovictor.gh.ui.screens.repositories.ListRepositoryScreen
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier
) {

    NavHost(navController = navController, startDestination = Screen.Repositories.route, modifier = modifier) {
        composable(Screen.Repositories.route){
            ListRepositoryScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
        composable(
            route = "${Screen.PullRequests.route}/{owner_name}/{repo_name}",
            arguments = listOf(
                navArgument("repo_name") { type = NavType.StringType },
                navArgument("owner_name") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val ownerName = navBackStackEntry.arguments?.getString("owner_name") ?: ""
            val repoName = navBackStackEntry.arguments?.getString("repo_name") ?: ""

            PullRequestsScreen(
                navController = navController,
                ownerName = ownerName,
                repoName = repoName,
                viewModel = hiltViewModel()
            )
        }
    }
}
