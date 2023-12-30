package com.hrmoller.newsapp.repositories.impl

import com.hrmoller.newsapp.models.Article
import com.hrmoller.newsapp.repositories.NewsArticleRepository
import com.hrmoller.newsapp.repositories.result.NewsArticleResult
import com.hrmoller.newsapp.services.NewsApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class NewsArticleRepositoryImpl @Inject constructor(private val newsApiService: NewsApiService) :
        NewsArticleRepository {

    private val _articles = MutableStateFlow(mutableListOf<Article>())
    override val articles: StateFlow<List<Article>>
        get() = _articles.asStateFlow()

    private var lastRetrievedPage = 1
    override suspend fun fetchTopHeadlines(): NewsArticleResult {
        var result: NewsArticleResult = NewsArticleResult.Error()

        return try {
            val res = newsApiService.getTopHeadlinesPage(lastRetrievedPage)

            when (res.isSuccessful) {
                true -> {
                    _articles.value = res.body()?.articles!!.filter { it.title != "[Removed]" }.toMutableList()
                    lastRetrievedPage++
                    result = NewsArticleResult.Success
                }

                false -> {
                    if (res.message().isNotEmpty()) {
                        result = NewsArticleResult.Error(res.message())
                    }
                    else if (res.code() == 426) result = NewsArticleResult.Error(
                        "Unable to load more articles. The free account type only " + "permits 100 " + "articles"
                    )

                }
            }
            result
        }
        catch (e: Exception) {
            result = NewsArticleResult.Error(e.message.orEmpty())
            result
        }
    }

    override suspend fun fetchAdditionalTopHeadlines(): NewsArticleResult {
        var result: NewsArticleResult = NewsArticleResult.Error()

        return try {
            val res = newsApiService.getTopHeadlinesPage(lastRetrievedPage)

            when (res.isSuccessful) {
                true -> {
                    _articles.value += res.body()?.articles!!.filter { it.title != "[Removed]" }.toMutableList()
                    lastRetrievedPage++
                    result = NewsArticleResult.Success
                }

                false -> {
                    if (res.message().isNotEmpty()) {
                        result = NewsArticleResult.Error(res.message())
                    }
                    else if (res.code() == 426) result = NewsArticleResult.Error(
                        "Unable to load more articles. The free account type only " + "permits 100 " + "articles"
                    )
                }
            }
            result
        }
        catch (e: Exception) {
            result = NewsArticleResult.Error(e.message.orEmpty())
            result
        }
    }

    override suspend fun getArticleByIndex(index: Int): Result<Article> {
        return when (val art = _articles.value.getOrNull(index)) {
            null -> Result.failure(Exception("Article Not Found"))
            else -> Result.success(art)
        }
    }
}