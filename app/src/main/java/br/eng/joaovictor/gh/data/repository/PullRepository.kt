package br.eng.joaovictor.gh.data.repository

import androidx.paging.PagingData
import br.eng.joaovictor.gh.data.model.Pull
import kotlinx.coroutines.flow.Flow

interface PullRepository {
    fun getPulls(owner: String, repo: String): Flow<PagingData<Pull>>
}