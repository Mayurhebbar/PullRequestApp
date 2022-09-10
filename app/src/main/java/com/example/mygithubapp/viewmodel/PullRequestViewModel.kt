package com.example.mygithubapp.viewmodel

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygithubapp.data.PullRequestResponse
import com.example.mygithubapp.ui.PullRequestAdapter
import com.example.mygithubapp.repo.PullRequestRepository
import com.example.mygithubapp.utils.NetworkUtil
import com.example.mygithubapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class PullRequestViewModel(private val pullRequestRepository: PullRequestRepository, private val context: Context, private val listener : PullRequestListener,private val networkUtil : NetworkUtil = NetworkUtil()) : ViewModel() {

    var pageNo = 0
    var owner = ""
    var repo = ""
    var listEndReached = false
    private var currDataList = arrayListOf<PullRequestResponse>()

    val pullRequestDetails : MutableLiveData<Resource<List<PullRequestResponse>>> = MutableLiveData()
    var pullRequestAdapter = PullRequestAdapter{
        //load next page .... triggered only when reaching end of page
        if(!listEndReached && currDataList.isNotEmpty()){
            getPRDetails(owner, repo, ++pageNo)
        }
    }

    fun getPRDetails(userName: String, repoName: String, pageNo : Int) {
        owner = userName
        repo = repoName
        if(networkUtil.isInternetAvailable(context)) {
            viewModelScope.launch(Dispatchers.IO) {
                pullRequestDetails.postValue(Resource.Loading())
                val response = pullRequestRepository.fetchPRDetails(userName, repoName, pageNo)
                pullRequestDetails.postValue(handleGithubResponse(response))
            }
        }
        else{
            listener.internetConnectionNotAvailable()
        }
    }

    private fun handleGithubResponse(response: Response<List<PullRequestResponse>>) :
            Resource<List<PullRequestResponse>> {
        if(response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    fun isFirstPage() = pageNo == 0

    fun initPRList(data : List<PullRequestResponse>?) {
        currDataList.clear()
        addPRList(data)
    }

    fun addPRList(data : List<PullRequestResponse>?){
        currDataList.addAll(data ?: arrayListOf())
        if(data.isNullOrEmpty()) listEndReached = true
        if(isFirstPage())
            pullRequestAdapter.setData(data)
        else
            pullRequestAdapter.addPRData(data)
    }

    @VisibleForTesting
    fun testHandleGitHubResponse(response: Response<List<PullRequestResponse>>) : Resource<List<PullRequestResponse>>{
        return handleGithubResponse(response)
    }

    @VisibleForTesting
    fun setFirstPage(pageNo: Int){
        this.pageNo = pageNo
    }

    @VisibleForTesting
    fun setAdapter(adapter: PullRequestAdapter){
        pullRequestAdapter = adapter
    }

    interface PullRequestListener{

        fun internetConnectionNotAvailable()
    }

}