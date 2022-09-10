package com.example.mygithubapp.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mygithubapp.data.PullRequestResponse
import com.example.mygithubapp.repo.PullRequestRepository
import com.example.mygithubapp.ui.PullRequestAdapter
import com.example.mygithubapp.utils.NetworkUtil
import com.example.mygithubapp.utils.Resource
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import retrofit2.Response

class PullRequestViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var instance : PullRequestViewModel

    private val pullRequestRepository : PullRequestRepository = mock()

    private val context : Context = mock()

    private val mPullRequestDetails : MutableLiveData<Resource<PullRequestResponse>> = mock()

    private val pullRequestListener : PullRequestViewModel.PullRequestListener = mock()

    private val network : NetworkUtil = mock()

    @Before
    fun setUp(){
        instance = PullRequestViewModel(pullRequestRepository,context,pullRequestListener,network)
    }

    @Test
    fun `test getPRDetails when internet is not available `() = runBlocking{
        whenever(network.isInternetAvailable(context)).thenReturn(false)
        instance.getPRDetails("", "", 0)
        instance.pullRequestDetails.observeForever(mock())
        delay(10)
        verify(mPullRequestDetails, never()).postValue(any())
    }

    @Test
    fun `test getPRDetails when internet is available `() = runBlocking{
        whenever(network.isInternetAvailable(context)).thenReturn(true)
        val result = instance.getPRDetails("", "", 0)
        instance.pullRequestDetails.observeForever(mock())
        delay(10)
        assertNotNull(result)
    }

    @Test
    fun `test handleGithubResponse response is false`(){
        var response : Response<List<PullRequestResponse>> = mock()
        whenever(response.isSuccessful).thenReturn(false)
        whenever(response.message()).thenReturn("abc")
        instance.testHandleGitHubResponse(response)
        verify(response).message()
    }

    @Test
    fun `test handleGithubResponse response is true response body is not null`(){
        var response : Response<List<PullRequestResponse>> = mock()
        whenever(response.isSuccessful).thenReturn(true)
        whenever(response.body()).thenReturn(listOf())
        instance.testHandleGitHubResponse(response)
        verify(response, never()).message()
    }

    @Test
    fun `test handleGithubResponse response is true response body is null`(){
        var response : Response<List<PullRequestResponse>> = mock()
        whenever(response.isSuccessful).thenReturn(true)
        whenever(response.body()).thenReturn(null)
        whenever(response.message()).thenReturn("abc")
        instance.testHandleGitHubResponse(response)
        verify(response).message()
    }

    @Test
    fun `test addPRList data is not null and isFirstPage is true `(){
        val adapter : PullRequestAdapter = mock()
        instance.setAdapter(adapter)
        instance.setFirstPage(0)
        instance.addPRList(listOf(mock()))
        verify(adapter).setData(any())
    }

    @Test
    fun `test addPRList data is empty and isFirstPage is false `(){
        val adapter : PullRequestAdapter = mock()
        instance.setAdapter(adapter)
        instance.setFirstPage(1)
        instance.addPRList(listOf())
        verify(adapter).addPRData(any())
    }
}