package com.example.mygithubapp.inject

import com.example.mygithubapp.repo.PullRequestRepository

object Injection {

    fun providePullRequestRepository() : PullRequestRepository{
        return PullRequestRepository()
    }
}