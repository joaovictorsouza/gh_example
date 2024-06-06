package br.eng.joaovictor.gh.data.repository

import androidx.paging.PagingData
import br.eng.joaovictor.gh.data.model.Repo
import kotlinx.coroutines.flow.Flow

interface RepoRepository {
    fun searchRepositories(): Flow<PagingData<Repo>>
}