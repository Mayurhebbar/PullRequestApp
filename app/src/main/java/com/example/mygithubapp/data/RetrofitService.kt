package com.example.mygithubapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    companion object {
        var BASE_URL = "https://api.github.com"

        private val retrofit by lazy {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
        }

        val retrofitApi by lazy {
            retrofit.create(GithubApi::class.java)
        }
    }
}