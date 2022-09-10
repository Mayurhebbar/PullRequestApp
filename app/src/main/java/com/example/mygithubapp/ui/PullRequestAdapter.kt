package com.example.mygithubapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mygithubapp.data.PullRequestResponse
import com.example.mygithubapp.R
import kotlinx.android.synthetic.main.row_pull_request.view.*

class PullRequestAdapter(private val nextPageCallback: () -> Unit) : RecyclerView.Adapter<PullRequestAdapter.PullRequestVH>() {

    private var pullRequestsList = arrayListOf<PullRequestResponse>()

    fun setData(data : List<PullRequestResponse>?) {
        if(data == null) return
        pullRequestsList.clear()
        pullRequestsList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PullRequestVH {
        return PullRequestVH(LayoutInflater.from(parent.context).inflate(R.layout.row_pull_request, parent, false))
    }

    override fun onBindViewHolder(holder: PullRequestVH, position: Int) {
        val currentPR = pullRequestsList[position]
        holder.itemView.pullRequestTitle.text = currentPR.title
        holder.itemView.createdDate.text = currentPR.createdDate?.split("T")?.get(0) ?: ""
        holder.itemView.closedDate.text = currentPR.closedDate?.split("T")?.get(0) ?: ""
        holder.itemView.userName.text = currentPR.user?.userName ?: ""
        currentPR.user?.userImageUrl?.let {
            Glide.with(holder.itemView.context)
                .load(it)
                .placeholder(R.drawable.ic_profile_placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.itemView.userImage)
        }
    }

    override fun getItemCount(): Int {
        return pullRequestsList.size
    }

    fun addPRData(data : List<PullRequestResponse>?){
        if(data == null) return
        pullRequestsList.addAll(data)
        notifyDataSetChanged()
    }

    inner class PullRequestVH(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onViewAttachedToWindow(holder: PullRequestVH) {
        super.onViewAttachedToWindow(holder)
        val size = pullRequestsList.size
        if(holder.adapterPosition == size-1) {
            nextPageCallback()
        }
    }

}