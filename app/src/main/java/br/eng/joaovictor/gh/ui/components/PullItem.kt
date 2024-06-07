package br.eng.joaovictor.gh.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import br.eng.joaovictor.gh.R
import br.eng.joaovictor.gh.data.model.Pull
import compose.icons.Octicons
import compose.icons.octicons.RepoForked16
import compose.icons.octicons.StarFill16

@Composable
fun PullItem(item: Pull, modifier: Modifier = Modifier) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .testTag("pull_item")
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OwnerDetails(
                imageUrl = item.user.avatarUrl,
                ownerName = item.user.login
            )
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = item.title,
                maxLines = 1,
                softWrap = true,
                modifier = Modifier.semantics { heading() }
            )

            Text(
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                softWrap = true,
                text = item.body ?: ""
            )
        }
    }
}