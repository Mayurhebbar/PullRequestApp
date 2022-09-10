package com.example.mygithubapp.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET(ApiEndpoints.GITHUB_PR_DETAILS)
    suspend fun getGithubPrDetails(@Path("owner") owner: String,
                                   @Path("repo") repo: String,
                                   @Query("state") state: String,
                                   @Query("page") page: Int,
                                   @Query("per_page") perPage: Int) :
            Response<List<PullRequestResponse>>
}