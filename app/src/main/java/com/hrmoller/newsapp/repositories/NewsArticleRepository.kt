package com.hrmoller.newsapp.repositories

import com.hrmoller.newsapp.models.Article
import com.hrmoller.newsapp.repositories.result.NewsArticleResult
import kotlinx.coroutines.flow.StateFlow

interface NewsArticleRepository {

    val articles: StateFlow<List<Article>>
    suspend fun fetchTopHeadlines(): NewsArticleResult
    suspend fun fetchAdditionalTopHeadlines(): NewsArticleResult
    suspend fun getArticleByIndex(index: Int): Result<Article>
}