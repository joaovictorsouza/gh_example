package br.eng.joaovictor.gh.di

import br.eng.joaovictor.gh.data.datasource.remote.ApiService
import br.eng.joaovictor.gh.data.repository.RepoRepository
import br.eng.joaovictor.gh.data.repository.RepoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    /**
     * Provides RemoteDataRepository for access api service method
     */
    @Singleton
    @Provides
    fun provideRepoRepository(
        apiService: ApiService,
    ): RepoRepository {
        return RepoRepositoryImpl(
            apiService
        )
    }

}