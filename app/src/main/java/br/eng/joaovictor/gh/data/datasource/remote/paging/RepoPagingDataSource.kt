package br.eng.joaovictor.gh.data.datasource.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.eng.joaovictor.gh.data.datasource.remote.ApiService
import br.eng.joaovictor.gh.data.model.Repo
import retrofit2.HttpException
import javax.inject.Inject

class RepoPagingDataSource @Inject constructor(private val apiService: ApiService): PagingSource<Int, Repo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        return try {
            val nextPage = params.key ?: 1
            val repoResult = apiService.search("language:java", "stars", nextPage)
            return LoadResult.Page(
                data = repoResult.items,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (repoResult.items.isNotEmpty()) nextPage + 1 else null
            )
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return state.anchorPosition
    }
}