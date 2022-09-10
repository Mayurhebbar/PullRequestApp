package com.example.mygithubapp.data

import com.google.gson.annotations.SerializedName



data class PullRequestResponse(
    @SerializedName("title") val title: String? = null,
    @SerializedName("created_at") val createdDate: String? = null,
    @SerializedName("closed_at") val closedDate: String? = null,
    @SerializedName("user") val user: User? = null,
)

data class User(
    @SerializedName("avatar_url") val userImageUrl: String? = null,
    @SerializedName("login") val userName: String? = null
)
