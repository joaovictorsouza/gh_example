package br.eng.joaovictor.gh.ui.components

import Undrawnotfound
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import br.eng.joaovictor.gh.R

@Composable
fun PageEmpty(onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
            .testTag("empty_page")) {

        Image(
            imageVector = Undrawnotfound,
            contentDescription = stringResource(R.string.txt_desc_not_found)
        )
        Text(
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(R.string.txt_not_found),
            textAlign = TextAlign.Center
        )
        TextButton(onClick = { onRetry() }) {
            Text(stringResource(R.string.btn_retry))
        }
    }
}