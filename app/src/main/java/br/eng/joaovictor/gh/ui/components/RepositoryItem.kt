package br.eng.joaovictor.gh.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.eng.joaovictor.gh.R
import br.eng.joaovictor.gh.data.model.Repo
import coil.compose.AsyncImage
import coil.request.ImageRequest
import compose.icons.Octicons
import compose.icons.octicons.RepoForked16
import compose.icons.octicons.StarFill16

@Composable
fun RepositoryItem(item: Repo, modifier: Modifier = Modifier) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .testTag("repo_item")
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OwnerDetails(
                imageUrl = item.owner.avatarUrl,
                ownerName = item.owner.login
            )
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = item.name,
                maxLines = 1,
                softWrap = true,
                modifier = Modifier.semantics { heading() }
            )

            Text(
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                softWrap = true,
                text = item.description ?: ""
            )
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                RepositoryCounter(
                    count = item.forksCount,
                    icon = Octicons.RepoForked16,
                    contentDescription = stringResource(
                        R.string.txt_fork_number,
                        item.name
                    )
                )
                RepositoryCounter(
                    count = item.stargazersCount,
                    icon = Octicons.StarFill16,
                    contentDescription = stringResource(R.string.txt_start_number, item.name)
                )
            }
        }
    }
}

@Composable
fun OwnerDetails(imageUrl: String, ownerName: String, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AsyncImage(
            clipToBounds = true,
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(
                R.string.txt_description_owner_image,
                ownerName
            ),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(24.dp)
                .clip(shape = CircleShape)
                .testTag("img_repo_owner")
        )
        Text(text = ownerName)
    }
}

@Composable
fun RepositoryCounter(
    count: Int,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(28.dp)
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
        Text(
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            text = count.toString(),
            textAlign = TextAlign.Center
        )
    }
}