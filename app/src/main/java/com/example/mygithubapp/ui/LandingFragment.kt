package com.example.mygithubapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mygithubapp.R
import com.example.mygithubapp.utils.NetworkUtil
import com.example.mygithubapp.utils.addFragmentBackStack
import com.example.mygithubapp.utils.isValid
import com.example.mygithubapp.utils.toast
import kotlinx.android.synthetic.main.fragment_landing.view.*

class LandingFragment : Fragment() {

    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate(R.layout.fragment_landing, container, false)
        onClicks()
        (requireActivity() as MainActivity).supportActionBar?.title =
            getString(R.string.home)
        return mView.rootView
    }

    private fun onClicks() {
        mView.btnContinue.setOnClickListener {
            if(NetworkUtil().isInternetAvailable(requireContext())) {
                val userName = mView.edtUserName.editableText.toString()
                val repoName = mView.edtRepoName.editableText.toString()
                if (userName.isValid() && repoName.isValid()) {
                    goToPullRequestFragment(userName, repoName)
                } else {
                    toast(getString(R.string.entire_fields_toast))
                }
            }
            else{
                toast(getString(R.string.no_connection_error))
            }
        }
    }

    private fun goToPullRequestFragment(userName: String,repoName :String) {
        addFragmentBackStack(
            R.id.mainFragmentContainer, PullRequestFragment.newInstance(userName,repoName), null
        )
    }

    companion object {
        fun newInstance(): LandingFragment {
            return LandingFragment()
        }
    }
}