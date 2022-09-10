package com.example.mygithubapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mygithubapp.R
import com.example.mygithubapp.utils.Resource
import com.example.mygithubapp.utils.toast
import com.example.mygithubapp.viewmodel.PullRequestViewModel
import com.example.mygithubapp.viewmodel.PullRequestViewModelProviderFactory
import com.example.mygithubapp.databinding.FragmentPullRequestsBinding
import com.example.mygithubapp.inject.Injection
import com.example.mygithubapp.utils.hide
import com.example.mygithubapp.utils.show
import kotlinx.android.synthetic.main.layout_error_empty_state.view.*

class PullRequestFragment : Fragment(),PullRequestViewModel.PullRequestListener {

    private lateinit var viewModel: PullRequestViewModel
    private lateinit var binding : FragmentPullRequestsBinding
    private var userName = ""
    private var repoName = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_pull_requests, container, false)
        val viewModelProviderFactory = PullRequestViewModelProviderFactory(Injection.providePullRequestRepository(),requireContext(),this)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[PullRequestViewModel::class.java]
        (requireActivity() as MainActivity).supportActionBar?.title =
            getString(R.string.pull_request_header_name)
        initBundle()
        initTasks()
        initBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = viewModel.pullRequestAdapter
    }

    private fun initBundle(){
        arguments?.getString(ARGS_USERNAME)?.let {
            userName = it
        }
        arguments?.getString(ARGS_REPO_NAME)?.let {
            repoName = it
        }
    }

    private fun init() {
        initApiObservers()
        binding.swipeRefresh.setOnRefreshListener {
            initTasks()
        }
    }

    private fun initTasks() {
        viewModel.pageNo = 0
        viewModel.listEndReached = false
        viewModel.getPRDetails(userName, repoName, viewModel.pageNo)
        binding.pullRequestContainer.show()
    }

    private fun initApiObservers() {
        viewModel.pullRequestDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    stopLoadingProgress()
                    response.data?.let {
                        if(it.isEmpty() && viewModel.isFirstPage()) {
                            showEmptyPRView()
                        }
                        else if(it.isEmpty() && !viewModel.isFirstPage()){
                            toast(getString(R.string.end_of_the_list_toast))
                        }
                        else {
                            binding.noPrEmptyState.emptyView.hide()
                            binding.pullRequestContainer.show()
                            if(viewModel.isFirstPage())
                                viewModel.initPRList(it)
                            else
                                viewModel.addPRList(it)
                        }
                    }
                }

                is Resource.Error -> {
                    stopLoadingProgress()
                    if(!viewModel.isFirstPage()){
                        toast(getString(R.string.something_went_wrong_toast))
                    }
                    else {
                        showConnectionErrorView()
                        binding.connectionErrorState.retryBtn.setOnClickListener {
                            binding.connectionErrorState.hide()
                            initTasks()
                        }
                    }
                }

                is Resource.Loading -> {
                    if(viewModel.isFirstPage())
                        binding.LoadingInProgress.show()
                    else
                        binding.paginationProgressBar.show()
                }
            }
        }
    }

    private fun stopLoadingProgress() {
        binding.LoadingInProgress.hide()
        binding.swipeRefresh.isRefreshing = false
        binding.paginationProgressBar.hide()
    }

    private fun showEmptyPRView(){
        binding.noPrEmptyState.emptyView.show()
        binding.pullRequestContainer.hide()
    }

    private fun showConnectionErrorView(){
        binding.connectionErrorState.show()
        binding.pullRequestContainer.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    companion object {
        const val TAG = "PullRequestFragment"
        private const val ARGS_USERNAME = "args_username"
        private const val ARGS_REPO_NAME = "args_repo_name"
        fun newInstance(userName : String, repoName :String): PullRequestFragment {
            val bundle = Bundle()
            bundle.putString(ARGS_USERNAME, userName)
            bundle.putString(ARGS_REPO_NAME, repoName)
            val fragment = PullRequestFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun internetConnectionNotAvailable() {
        toast(getString(R.string.no_connection_error))
    }
}