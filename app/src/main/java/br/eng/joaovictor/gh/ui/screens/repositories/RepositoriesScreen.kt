package br.eng.joaovictor.gh.ui.screens.repositories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import br.eng.joaovictor.gh.R
import br.eng.joaovictor.gh.ui.components.CardError
import br.eng.joaovictor.gh.ui.components.RepositoryItem
import compose.icons.Octicons
import compose.icons.octicons.MarkGithub16

@ExperimentalMaterial3Api
@Composable
fun ListRepositoryScreen(viewModel: RepositoriesViewModel = hiltViewModel()) {
    val items = viewModel.repos().collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary),
                title = { Text(stringResource(R.string.text_list_repos_screen)) },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        imageVector = Octicons.MarkGithub16, contentDescription = stringResource(R.string.text_list_repos_screen))
                }
                )
             },
        modifier = Modifier.fillMaxSize())
    { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .testTag("repository_list")
        ) {

            if(items.loadState.refresh is LoadState.Loading) {
                item {
                    Row(
                        modifier = Modifier.fillParentMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp)
                                .testTag("loading")
                        )
                    }
                }
            }

                if (items.loadState.refresh is LoadState.Error) {
                    item {
                        CardError(
                            modifier = Modifier.testTag("error_loading"),
                            loadState = items.loadState.refresh,
                            retry = { items.retry() }
                        )
                    }
                }


                items(
                    count = items.itemCount,
                    key = items.itemKey { it.id }
                ) { index ->
                    val repo = items[index]
                    if (repo != null) {
                        RepositoryItem(
                            modifier = Modifier.fillParentMaxWidth(),
                            item = repo
                        )
                    }
                }

                if (items.loadState.append is LoadState.Loading) {
                    item {
                        Row(
                            modifier = Modifier.fillParentMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.testTag("loading_more"))
                        }
                    }
                }


                if (items.loadState.append is LoadState.Error) {
                    item {
                        CardError(
                            modifier = Modifier.testTag("error_loading_more"),
                            loadState = items.loadState.append,
                            retry = { items.retry() }
                        )
                    }
                }
            }

    }
}
