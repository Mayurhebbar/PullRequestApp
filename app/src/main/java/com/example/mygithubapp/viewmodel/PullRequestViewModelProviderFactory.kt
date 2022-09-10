package com.example.mygithubapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mygithubapp.repo.PullRequestRepository

class PullRequestViewModelProviderFactory(private val pullRequestRepository: PullRequestRepository, private val context : Context, private val listener : PullRequestViewModel.PullRequestListener
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PullRequestViewModel(pullRequestRepository,context,listener) as T
    }
}
