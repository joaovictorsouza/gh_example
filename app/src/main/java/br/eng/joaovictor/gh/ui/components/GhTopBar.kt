package br.eng.joaovictor.gh.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.eng.joaovictor.gh.R
import compose.icons.Octicons
import compose.icons.octicons.ArrowLeft24
import compose.icons.octicons.MarkGithub16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GhTopBar(
    onBack: () -> Unit = {},
    isBackEnabled: Boolean = false,
    title: String = stringResource(R.string.title_screen_repo)
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = { Text(title) },
        navigationIcon = {
            if (isBackEnabled) {
                Icon(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(32.dp)
                        .clickable(onClickLabel = stringResource(R.string.txt_menu_back)) {
                            onBack()
                        },
                    tint = MaterialTheme.colorScheme.onPrimary,
                    imageVector = Octicons.ArrowLeft24,
                    contentDescription = stringResource(R.string.txt_menu_back)
                )
            } else {
                Icon(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(32.dp),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    imageVector = Octicons.MarkGithub16,
                    contentDescription = stringResource(R.string.txt_desc_github)
                )
            }
        }
    )
}
