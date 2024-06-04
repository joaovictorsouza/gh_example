package br.eng.joaovictor.gh.navigation

import androidx.annotation.StringRes
import br.eng.joaovictor.gh.R

sealed class Screen(
    val route: String,
    @StringRes val title: Int = R.string.app_name,
    val objectName: String = "",
    val objectPath: String = ""
) {
    data object Repositories: Screen("repositories", R.string.title_screen_repo, "Repositories", "RepositoriesScreen")
}