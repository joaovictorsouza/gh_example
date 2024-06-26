package br.eng.joaovictor.gh

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import retrofit2.Response
import java.time.Duration
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTestApi::class)
class ListRepositoryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var apiService: ApiService
    private lateinit var repoRepository: RepoRepository
    private lateinit var viewModel: RepositoriesViewModel

    @Before
    fun setup() {
        apiService = mockk()
        repoRepository = RepoRepositoryImpl(apiService)
        viewModel = RepositoriesViewModel(repoRepository)
    }

    private fun setMockApiResult(apiResult: ApiResult<Repo>) {
        coEvery { apiService.search(any(), any(), any()) }.returns(apiResult)
    }

    private fun setMockApiError(error: Throwable) {
        coEvery { apiService.search(any(), any(), any()) }.throws(error)
    }

    private fun launchScreen() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            GithubTestTheme {
                ListRepositoryScreen(viewModel, navController)
            }
        }
    }

    @Test
    fun displayAppBarTitle() = runTest {
        launchScreen()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        composeTestRule.onNodeWithText(appContext.getString(R.string.title_screen_repo)).assertIsDisplayed()
    }

    @Test
    fun screen_loading_checkIfProgressBarIsDisplayed() = runTest {
        coEvery { apiService.search(any(), any(), any()) }.coAnswers {
            delay(Duration.ofSeconds(2))
            ApiResult(0, false, emptyList())
        }

        launchScreen()
        composeTestRule.onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun screen_loadingHttpError_checkIfCardPageIsDisplayed() = runTest {
        val errorResponse = Response.error<String>(403, "Error".toResponseBody("text/plain".toMediaType()))
        setMockApiError(HttpException(errorResponse))
        launchScreen()
        composeTestRule.onNodeWithTag("error_page").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_emptyList_checkIfEmptyPageIsDisplayed() = runTest {
        setMockApiResult(ApiResult(0, false, emptyList()))
        launchScreen()
        composeTestRule.onNodeWithTag("empty_page").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_onePage_checkIfAllItemsAreDisplayed() = runTest {
        setMockApiResult(ApiResult(repoListPage1.size, false, repoListPage1))
        launchScreen()
        repoListPage1.forEach { repo ->
            composeTestRule.onNodeWithTag("lazy_list").performScrollToKey(repo.hashCode()).assertIsDisplayed()
        }
    }

    @Test
    fun lazyColumn_twoPages_checkIfAllItemsAreDisplayed() = runTest {
        coEvery { apiService.search(any(), any(), 1) }.returns(ApiResult(repoListPage1.size, false, repoListPage1))
        coEvery { apiService.search(any(), any(), 2) }.returns(ApiResult(repoListPage2.size, false, repoListPage2))
        launchScreen()
        repoListPage1.forEach { repo ->
            composeTestRule.onNodeWithTag("lazy_list").performScrollToKey(repo.hashCode()).assertIsDisplayed()
        }
        repoListPage2.forEach { repo ->
            composeTestRule.onNodeWithTag("lazy_list").performScrollToKey(repo.hashCode()).assertIsDisplayed()
        }
    }

    @Test
    fun lazyColumn_LoadMore_checkIfProgressBarIsDisplayed() = runTest {
        coEvery { apiService.search(any(), any(), 1) }.returns(ApiResult(repoListPage1.size, false, repoListPage1))
        coEvery { apiService.search(any(), any(), 2) }.coAnswers {
            delay(Duration.ofSeconds(2))
            ApiResult(repoListPage2.size, false, repoListPage2)
        }
        launchScreen()
        repoListPage1.forEach { repo ->
            composeTestRule.onNodeWithTag("lazy_list").performScrollToKey(repo.hashCode()).assertIsDisplayed()
        }
        composeTestRule.onNodeWithTag("loading_more").assertIsDisplayed()
    }

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("br.eng.joaovictor.gh", appContext.packageName)
    }
}
