package br.eng.joaovictor.gh.navigation

import androidx.annotation.StringRes
import br.eng.joaovictor.gh.R

sealed class Screen(
    val route: String
) {
    data object Repositories: Screen("repositories")
    data object PullRequests: Screen("pull_requests")
}