package br.eng.joaovictor.gh.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.eng.joaovictor.gh.data.datasource.remote.ApiService
import br.eng.joaovictor.gh.data.datasource.remote.paging.RepoPagingDataSource
import br.eng.joaovictor.gh.data.model.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(private val apiService: ApiService): RepoRepository {
    override fun searchRepositories(): Flow<PagingData<Repo>> = Pager(
        config = PagingConfig(pageSize = 30),
        pagingSourceFactory = { RepoPagingDataSource(apiService) }
    ).flow
}