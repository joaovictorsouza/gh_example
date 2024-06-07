package br.eng.joaovictor.gh.ui.screens.pull_requests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import br.eng.joaovictor.gh.data.repository.PullRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class PullRequestsViewModel @Inject constructor(
    private val pullRepository: PullRepository
) : ViewModel() {
    fun pulls(repoName: String, ownerName: String) =
        pullRepository.getPulls(ownerName, repoName)
            .cachedIn(viewModelScope)
}
