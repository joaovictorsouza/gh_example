package br.eng.joaovictor.gh

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.testing.asSnapshot
import br.eng.joaovictor.gh.data.datasource.remote.ApiService
import br.eng.joaovictor.gh.data.datasource.remote.paging.RepoPagingDataSource
import br.eng.joaovictor.gh.data.model.ApiResult
import br.eng.joaovictor.gh.data.model.Repo
import br.eng.joaovictor.gh.data.repository.RepoRepository
import br.eng.joaovictor.gh.data.repository.RepoRepositoryImpl
import br.eng.joaovictor.gh.utils.repoListPage1
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RepoRepositoryTest {

    private lateinit var apiService: ApiService
    private lateinit var repoRepository: RepoRepositoryImpl

    @Before
    fun setup() {
        apiService = mockk()
        repoRepository = RepoRepositoryImpl(apiService)
        mockkStatic(Log::class)
        every { Log.isLoggable(any(), any()) } returns false
    }

    @Test
    fun `searchRepositories returns expected data`() = runTest {
        val resultList = ApiResult<Repo>(
            items = repoListPage1,
            totalCount = repoListPage1.size,
            incompleteResults = false
        )

        val resultEmpty = ApiResult<Repo>(
            items = emptyList(),
            totalCount = 0,
            incompleteResults = false
        )

        coEvery { apiService.search(any(), any(), 1) } returns resultList
        coEvery { apiService.search(any(), any(), match{ it != 1}) } returns resultEmpty

        val result = repoRepository.searchRepositories()

        assertEquals(repoListPage1, result.asSnapshot {
        })
    }

    @Test
    fun `searchRepositories handles empty data`() = runTest {
        val resultEmpty = ApiResult<Repo>(
            items = emptyList(),
            totalCount = 0,
            incompleteResults = false
        )

        coEvery { apiService.search(any(), any(), any()) } returns resultEmpty

        val result = repoRepository.searchRepositories()

        Assert.assertEquals(emptyList<Repo>(), result.asSnapshot {  })
    }
}
