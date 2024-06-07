package br.eng.joaovictor.gh.data.datasource.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.eng.joaovictor.gh.data.datasource.remote.ApiService
import br.eng.joaovictor.gh.data.model.Pull
import retrofit2.HttpException

class PullPagingDataSource(
    private val apiService: ApiService,
    private val ownerName: String,
    private val repoName: String
): PagingSource<Int, Pull>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pull> {
        return try {
            val nextPage = params.key ?: 1
            val pullsResult = apiService.getPullRequests(ownerName, repoName, nextPage)
            return LoadResult.Page(
                data = pullsResult,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (pullsResult.isNotEmpty()) nextPage + 1 else null
            )
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Pull>): Int? {
        return state.anchorPosition
    }
}
