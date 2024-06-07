package br.eng.joaovictor.gh.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey

@Composable
fun <T : Any> PagingList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<T>,
    itemContent: @Composable (T) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("lazy_list")
    ) {

        items(
            count = items.itemCount,
            key = items.itemKey { it.hashCode() }  // or use a unique key from the item
        ) { index ->
            val item = items[index]
            if (item != null) {
                itemContent(item)
            }
        }

        if (items.loadState.append is LoadState.Loading) {
            item {
                Row(
                    modifier = Modifier
                        .fillParentMaxWidth()
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
                    onRetry = { items.retry() }
                )
            }
        }
    }
}