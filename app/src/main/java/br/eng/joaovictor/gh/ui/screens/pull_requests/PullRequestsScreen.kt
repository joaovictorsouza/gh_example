package br.eng.joaovictor.gh.ui.screens.pull_requests

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import br.eng.joaovictor.gh.R
import br.eng.joaovictor.gh.ui.components.GhTopBar
import br.eng.joaovictor.gh.ui.components.PageEmpty
import br.eng.joaovictor.gh.ui.components.PageError
import br.eng.joaovictor.gh.ui.components.PageLoading
import br.eng.joaovictor.gh.ui.components.PageNotConnected
import br.eng.joaovictor.gh.ui.components.PagingList
import br.eng.joaovictor.gh.ui.components.PullItem
import br.eng.joaovictor.gh.utils.network.ConnectionState
import br.eng.joaovictor.gh.utils.network.connectivityState

@Composable
fun PullRequestsScreen(
    navController: NavHostController,
    repoName: String,
    ownerName: String,
    viewModel: PullRequestsViewModel
) {
    val items = viewModel.pulls(repoName, ownerName).collectAsLazyPagingItems()
    val connection by connectivityState()
    val snackbarHostState = remember { SnackbarHostState() }
    val isConnected = connection === ConnectionState.Available


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            GhTopBar(
                title = repoName,
                isBackEnabled = true,
                onBack = { navController.popBackStack() }
            )
        },
    ) { innerPadding ->
        if(isConnected) {
            when (items.loadState.refresh) {
                is LoadState.NotLoading -> {
                    if (items.itemCount == 0) PageEmpty(onRetry = { items.refresh() })
                    else {
                        PagingList(
                            modifier = Modifier.padding(innerPadding),
                            items = items,
                        ) { pull ->
                            PullItem(item = pull, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }

                is LoadState.Loading -> PageLoading(modifier = Modifier.padding(innerPadding))
                is LoadState.Error -> PageError(onRetry = { items.retry() })
            }
        } else {
            PageNotConnected(onRetry = { items.retry() })
        }
    }
}
