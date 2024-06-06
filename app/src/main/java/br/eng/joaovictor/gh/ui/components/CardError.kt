package br.eng.joaovictor.gh.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import br.eng.joaovictor.gh.R
import retrofit2.HttpException
import java.io.IOException

@Composable
fun CardError(loadState: LoadState, retry: () -> Unit, modifier: Modifier) {
    val error = loadState.let { it as LoadState.Error }.error
    val message = when(error){
        is HttpException -> stringResource(id = R.string.error_http_exception)
        else -> stringResource(id = R.string.error_io_exception)
    }

    ElevatedCard(
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onError,
            containerColor = MaterialTheme.colorScheme.error
            ),
        modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = message)
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    onClick = retry) {
                    Text(text = stringResource(R.string.btn_retry))
                }
            }
        }
    }
}