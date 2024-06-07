package br.eng.joaovictor.gh

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.eng.joaovictor.gh.data.datasource.remote.ApiService
import br.eng.joaovictor.gh.data.model.ApiResult
import br.eng.joaovictor.gh.data.model.Pull
import br.eng.joaovictor.gh.data.model.Repo
import br.eng.joaovictor.gh.data.repository.PullRepository
import br.eng.joaovictor.gh.data.repository.PullRepositoryImpl
import br.eng.joaovictor.gh.data.repository.RepoRepository
import br.eng.joaovictor.gh.data.repository.RepoRepositoryImpl
import br.eng.joaovictor.gh.ui.screens.pull_requests.PullRequestsScreen
import br.eng.joaovictor.gh.ui.screens.pull_requests.PullRequestsViewModel
import br.eng.joaovictor.gh.ui.screens.repositories.ListRepositoryScreen
import br.eng.joaovictor.gh.ui.screens.repositories.RepositoriesViewModel
import br.eng.joaovictor.gh.ui.theme.GithubTestTheme
import br.eng.joaovictor.gh.utils.pullListPage1
import br.eng.joaovictor.gh.utils.pullListPage2
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
class PullRequestsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var apiService: ApiService
    private lateinit var repoRepository: PullRepository
    private lateinit var viewModel: PullRequestsViewModel

    @Before
    fun setup() {
        apiService = mockk()
        repoRepository = PullRepositoryImpl(apiService)
        viewModel = PullRequestsViewModel(repoRepository)
    }

    private fun setMockApiResult(list: List<Pull>) {
        coEvery { apiService.getPullRequests(any(), any(), any()) }.returns(list)
    }

    private fun setMockApiError(error: Throwable) {
        coEvery { apiService.search(any(), any(), any()) }.throws(error)
    }

    private fun launchScreen() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            GithubTestTheme {
                PullRequestsScreen(
                    ownerName = "User1",
                    repoName = "Repo1",
                    viewModel = viewModel,
                    navController = navController
                )
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
        coEvery { apiService.getPullRequests(any(), any(), any()) }.coAnswers {
            delay(Duration.ofSeconds(2))
            emptyList()
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
        setMockApiResult(emptyList())
        launchScreen()
        composeTestRule.onNodeWithTag("empty_page").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_onePage_checkIfAllItemsAreDisplayed() = runTest {
        setMockApiResult(pullListPage1)
        launchScreen()
        pullListPage1.forEach { pull ->
            composeTestRule.onNodeWithTag("lazy_list").performScrollToKey(pull.hashCode()).assertIsDisplayed()
        }
    }

    @Test
    fun lazyColumn_twoPages_checkIfAllItemsAreDisplayed() = runTest {
        coEvery { apiService.getPullRequests(any(), any(), 1) }.returns(pullListPage1)
        coEvery { apiService.getPullRequests(any(), any(), 2) }.returns(pullListPage2)
        launchScreen()
        pullListPage1.forEach { pull ->
            composeTestRule.onNodeWithTag("lazy_list").performScrollToKey(pull.hashCode()).assertIsDisplayed()
        }
        pullListPage2.forEach { pull ->
            composeTestRule.onNodeWithTag("lazy_list").performScrollToKey(pull.hashCode()).assertIsDisplayed()
        }
    }

    @Test
    fun lazyColumn_LoadMore_checkIfProgressBarIsDisplayed() = runTest {
        coEvery { apiService.getPullRequests(any(), any(), 1) }.returns(pullListPage1)
        coEvery { apiService.getPullRequests(any(), any(), 2) }.coAnswers {
            delay(Duration.ofSeconds(2))
            pullListPage2
        }
        launchScreen()
        pullListPage1.forEach { pull ->
            composeTestRule.onNodeWithTag("lazy_list").performScrollToKey(pull.hashCode()).assertIsDisplayed()
        }
        composeTestRule.onNodeWithTag("loading_more").assertIsDisplayed()
    }

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("br.eng.joaovictor.gh", appContext.packageName)
    }
}
