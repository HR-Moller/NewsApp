package com.hrmoller.newsapp.services

import com.hrmoller.newsapp.models.TopHeadlinesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines?language=en&pageSize=20")
    suspend fun getTopHeadlinesPage(@Query("page") page: Int): Response<TopHeadlinesResponse>
}