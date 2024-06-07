package br.eng.joaovictor.gh

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.testing.asSnapshot
import br.eng.joaovictor.gh.data.datasource.remote.ApiService
import br.eng.joaovictor.gh.data.datasource.remote.paging.RepoPagingDataSource
import br.eng.joaovictor.gh.data.model.ApiResult
import br.eng.joaovictor.gh.data.model.Pull
import br.eng.joaovictor.gh.data.model.Repo
import br.eng.joaovictor.gh.data.repository.PullRepository
import br.eng.joaovictor.gh.data.repository.PullRepositoryImpl
import br.eng.joaovictor.gh.data.repository.RepoRepository
import br.eng.joaovictor.gh.data.repository.RepoRepositoryImpl
import br.eng.joaovictor.gh.utils.pullListPage1
import br.eng.joaovictor.gh.utils.pullListPage2
import br.eng.joaovictor.gh.utils.repoListPage1
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PullRepositoryTest {

    private lateinit var apiService: ApiService
    private lateinit var pullRepository: PullRepository

    @Before
    fun setup() {
        apiService = mockk()
        pullRepository = PullRepositoryImpl(apiService)
        mockkStatic(Log::class)
        every { Log.isLoggable(any(), any()) } returns false
    }

    @Test
    fun `searchRepositories returns expected data`() = runTest {
        coEvery { apiService.getPullRequests(any(), any(), 1) } returns pullListPage1
        coEvery { apiService.getPullRequests(any(), any(), match{ it != 1}) } returns emptyList()

        val result = pullRepository.getPulls("owner", "repo")

        assertEquals(pullListPage1, result.asSnapshot {
        })
    }

    @Test
    fun `searchRepositories handles empty data`() = runTest {

        coEvery { apiService.getPullRequests(any(), any(), any()) } returns emptyList()

        val result = pullRepository.getPulls("owner", "repo")

        Assert.assertEquals(emptyList<Pull>(), result.asSnapshot {  })
    }


    @Test
    fun `searchRepositories throws exception when API call fails`() = runTest {
        coEvery { apiService.search(any(), any(), any()) } throws RuntimeException()

        try {
            val result = pullRepository.getPulls("owner", "repo").first()
        } catch (e: Exception) {
            assert(e is RuntimeException)
        }
    }
}
