package br.eng.joaovictor.gh.data.datasource.remote

import br.eng.joaovictor.gh.data.model.ApiResult
import br.eng.joaovictor.gh.data.model.Repo
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @GET("search/repositories")
    suspend fun search(
        @Query("q") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
    ): ApiResult<Repo>
}