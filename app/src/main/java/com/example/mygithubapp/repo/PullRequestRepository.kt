package com.example.mygithubapp.repo

import com.example.mygithubapp.data.RetrofitService

const val GITHUB_API_RESPONSE_SIZE = 10

class PullRequestRepository {

    suspend fun fetchPRDetails(owner: String, repo: String, pageNo : Int, perPage : Int = GITHUB_API_RESPONSE_SIZE) =
        RetrofitService.retrofitApi.getGithubPrDetails(owner, repo, "closed", pageNo, perPage)
}