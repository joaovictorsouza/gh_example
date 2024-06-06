package br.eng.joaovictor.gh

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToKey
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.eng.joaovictor.gh.data.datasource.remote.ApiService
import br.eng.joaovictor.gh.data.model.ApiResult
import br.eng.joaovictor.gh.data.model.Repo
import br.eng.joaovictor.gh.data.repository.RepoRepository
import br.eng.joaovictor.gh.data.repository.RepoRepositoryImpl
import br.eng.joaovictor.gh.ui.screens.repositories.ListRepositoryScreen
import br.eng.joaovictor.gh.ui.screens.repositories.RepositoriesViewModel
import br.eng.joaovictor.gh.ui.theme.GithubTestTheme
import br.eng.joaovictor.gh.utils.repoListPage1
import br.eng.joaovictor.gh.utils.repoListPage2
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.time.delay
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.time.Duration

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTestApi::class)
class ListRepositoryScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var apiService: ApiService
    private lateinit var repoRepository: RepoRepository
    private lateinit var viewModel: RepositoriesViewModel


    @Before
    fun beforeEach() {
        val contentType = "application/json".toMediaType()

        val json = Json {
            ignoreUnknownKeys = true
        }
        apiService = mockk()
        repoRepository = RepoRepositoryImpl(apiService)
        viewModel = RepositoriesViewModel(repoRepository)
    }


    @Test
    fun displayAppBarTitle() {
        val resultDto = ApiResult(
            totalCount = 0,
            incompleteResults = false,
            items = emptyList<Repo>()
        )

        coEvery { apiService.search(any(), any(), 1) }.returnsMany(resultDto)
        composeTestRule.setContent {
             GithubTestTheme {
                 ListRepositoryScreen(viewModel)
            }
        }

        composeTestRule.onNodeWithText("Java no Github").assertIsDisplayed()
    }

    @Test
    fun screen_loading_checkIfProgressBarIsDisplayed() = runTest {
        val resultDto = ApiResult(
            totalCount = 0,
            incompleteResults = false,
            items = emptyList<Repo>()
        )

        coEvery { apiService.search(any(), any(), 1) }.coAnswers {
            delay(Duration.ofSeconds(2))
            resultDto
        }

        composeTestRule.setContent {
            GithubTestTheme {
                ListRepositoryScreen(viewModel)
            }
        }

        composeTestRule.onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun screen_loadingHttpError_checkIfCardErrorIsDisplayed() = runTest {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val errorResponse = Response.error<String>(403, "Error".toResponseBody("text/plain".toMediaType()))
        val error = HttpException(errorResponse)
        coEvery { apiService.search(any(), any(), 1) }.throws(error)

        composeTestRule.setContent {
            GithubTestTheme {
                ListRepositoryScreen(viewModel)
            }
        }

        composeTestRule.onNodeWithTag("error_loading").assertIsDisplayed()
        composeTestRule.onNodeWithText(appContext.getString(R.string.error_http_exception)).assertIsDisplayed()
    }

    @Test
    fun screen_loadingUnknownError_checkIfCardErrorIsDisplayed() = runTest {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val error = IOException("exc")
        coEvery { apiService.search(any(), any(), 1) }.throws(error)

        composeTestRule.setContent {
            GithubTestTheme {
                ListRepositoryScreen(viewModel)
            }
        }

        composeTestRule.onNodeWithTag("error_loading").assertIsDisplayed()
        composeTestRule.onNodeWithText(appContext.getString(R.string.error_io_exception)).assertIsDisplayed()
    }

    @Test
    fun lazyColumn_onePage_checkIfAllItemsAreDisplayed() {
        val firstPageDto = ApiResult(
            totalCount = repoListPage1.size,
            incompleteResults = false,
            items = repoListPage1
        )

        val emptyPageDto = ApiResult(
            totalCount = 0,
            incompleteResults = false,
            items = emptyList<Repo>()
        )

        coEvery { apiService.search(any(), any(), 1) }.returnsMany(firstPageDto)
        coEvery { apiService.search(any(), any(), match{ it != 1}) }.returnsMany(emptyPageDto)

        repoRepository = RepoRepositoryImpl(apiService)
        viewModel = RepositoriesViewModel(repoRepository)

        composeTestRule.setContent {
            GithubTestTheme {
                ListRepositoryScreen(viewModel)
            }
        }

        repoListPage1.forEach{ repo ->
            composeTestRule.onNodeWithTag("repository_list").performScrollToKey(repo.id).assertIsDisplayed()
        }
    }


    @Test
    fun lazyColumn_twoPages_checkIfAllItemsAreDisplayed() {
        val firstPageDto = ApiResult(
            totalCount = repoListPage1.size,
            incompleteResults = false,
            items = repoListPage1
        )

        val secondPageDto = ApiResult(
            totalCount = repoListPage2.size,
            incompleteResults = false,
            items = repoListPage2
        )

        val emptyPageDto = ApiResult(
            totalCount = 0,
            incompleteResults = false,
            items = emptyList<Repo>()
        )

        coEvery { apiService.search(any(), any(), 1) }.returnsMany(firstPageDto)
        coEvery { apiService.search(any(), any(), 2) }.returnsMany(secondPageDto)
        coEvery { apiService.search(any(), any(), 3) }.returnsMany(emptyPageDto)

        repoRepository = RepoRepositoryImpl(apiService)
        viewModel = RepositoriesViewModel(repoRepository)

        composeTestRule.setContent {
            GithubTestTheme {
                ListRepositoryScreen(viewModel)
            }
        }

        repoListPage1.forEach{ repo ->
            composeTestRule.onNodeWithTag("repository_list").performScrollToKey(repo.id).assertIsDisplayed()
        }
        repoListPage2.forEach { repo ->
            composeTestRule.onNodeWithTag("repository_list").performScrollToKey(repo.id).assertIsDisplayed()
        }
    }

    @Test
    fun lazyColumn_LoadMore_checkIfProgressBarIsDisplayed() {
        val firstPageDto = ApiResult(
            totalCount = repoListPage1.size,
            incompleteResults = false,
            items = repoListPage1
        )

        val secondPageDto = ApiResult(
            totalCount = repoListPage2.size,
            incompleteResults = false,
            items = repoListPage2
        )
        coEvery { apiService.search(any(), any(), 1) }.returnsMany(firstPageDto)
        coEvery { apiService.search(any(), any(), 2) }.coAnswers{
            delay(Duration.ofSeconds(2))
            secondPageDto
        }

        repoRepository = RepoRepositoryImpl(apiService)
        viewModel = RepositoriesViewModel(repoRepository)

        composeTestRule.setContent {
            GithubTestTheme {
                ListRepositoryScreen(viewModel)
            }
        }

        repoListPage1.forEach{ repo ->
            composeTestRule.onNodeWithTag("repository_list").performScrollToKey(repo.id).assertIsDisplayed()
        }
        composeTestRule.onNodeWithTag("loading_more").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_LoadMoreHttpError_checkIfErrorCardIsDisplayed() {
        val firstPageDto = ApiResult(
            totalCount = repoListPage1.size,
            incompleteResults = false,
            items = repoListPage1
        )
        val errorResponse = Response.error<String>(403, "Error".toResponseBody("text/plain".toMediaType()))
        val error = HttpException(errorResponse)

        coEvery { apiService.search(any(), any(), 1) }.returnsMany(firstPageDto)
        coEvery { apiService.search(any(), any(), 2) }.throws(error)

        repoRepository = RepoRepositoryImpl(apiService)
        viewModel = RepositoriesViewModel(repoRepository)

        composeTestRule.setContent {
            GithubTestTheme {
                ListRepositoryScreen(viewModel)
            }
        }

        repoListPage1.forEach{ repo ->
            composeTestRule.onNodeWithTag("repository_list").performScrollToKey(repo.id)
        }
        composeTestRule.onNodeWithTag("error_loading_more").assertIsDisplayed()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("br.eng.joaovictor.gh", appContext.packageName)
    }
}