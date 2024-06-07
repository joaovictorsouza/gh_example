package br.eng.joaovictor.gh.ui.components

import Undrawconnection
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import br.eng.joaovictor.gh.R

@Composable
fun PageNotConnected(onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()) {

        Image(
            imageVector = Undrawconnection,
            contentDescription = stringResource(R.string.error_exception)
        )
        Text(
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(R.string.txt_no_internet),
            textAlign = TextAlign.Center
        )
        OutlinedButton(
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
            onClick = { onRetry() }) {
            Text(stringResource(R.string.btn_retry))
        }
    }
}