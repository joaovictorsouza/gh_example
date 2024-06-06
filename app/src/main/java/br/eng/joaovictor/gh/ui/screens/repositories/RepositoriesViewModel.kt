package br.eng.joaovictor.gh.ui.screens.repositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import br.eng.joaovictor.gh.data.repository.RepoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(private val repoRepository: RepoRepository) : ViewModel() {
    fun repos() =
        repoRepository.searchRepositories()
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}