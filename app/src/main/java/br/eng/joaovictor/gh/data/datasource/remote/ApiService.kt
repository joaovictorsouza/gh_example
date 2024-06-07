package br.eng.joaovictor.gh.data.datasource.remote

import br.eng.joaovictor.gh.BuildConfig
import br.eng.joaovictor.gh.data.model.ApiResult
import br.eng.joaovictor.gh.data.model.Pull
import br.eng.joaovictor.gh.data.model.Repo
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/repositories")
    @Headers("Authorization: Bearer ${BuildConfig.API_KEY}")
    suspend fun search(
        @Query("q") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
    ): ApiResult<Repo>

    @GET("repos/{owner}/{repo}/pulls")
    @Headers("Authorization: Bearer ${BuildConfig.API_KEY}")
    suspend fun getPullRequests(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("page") page: Int,
    ): List<Pull>
}