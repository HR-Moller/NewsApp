package com.hrmoller.newsapp

import com.hrmoller.newsapp.models.Article
import com.hrmoller.newsapp.models.Source
import com.hrmoller.newsapp.models.TopHeadlinesResponse
import com.hrmoller.newsapp.repositories.impl.NewsArticleRepositoryImpl
import com.hrmoller.newsapp.repositories.result.NewsArticleResult
import com.hrmoller.newsapp.services.NewsApiService
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class NewsArticleRepositoryTest {
    @RelaxedMockK
    private val newsApiService: NewsApiService = mockk<NewsApiService>(relaxed = true)

    private val newsArticleRepository = NewsArticleRepositoryImpl(newsApiService)

    @Test
    fun `fetchTopHeadlines success`() = runBlocking {
        val articles = listOf(
            Article(
                title = "Test Article",
                author = null,
                content = null,
                description = null,
                publishedAt = "tempus",
                source = Source(id = null, name = null),
                url = "https://search.yahoo.com/search?p=percipit",
                urlToImage = null
            )
        )
        coEvery { newsApiService.getTopHeadlinesPage(1) } returns Response.success(
            TopHeadlinesResponse(
                articles, status = "porro", totalResults = 1491
            )
        )

        val result = newsArticleRepository.fetchTopHeadlines()

        assertEquals(NewsArticleResult.Success, result)
        assertEquals(articles, newsArticleRepository.articles.value)
    }

    @Test
    fun `fetchTopHeadlines network error`() = runBlocking {
        coEvery { newsApiService.getTopHeadlinesPage(1) } throws IOException("Network error")

        val result = newsArticleRepository.fetchTopHeadlines()

        assertTrue(result is NewsArticleResult.Error)
    }

    @Test
    fun `fetchAdditionalTopHeadlines success`() = runBlocking {
        val additionalArticles = listOf(
            Article(
                title = "Additional Test Article",
                author = null,
                content = null,
                description = null,
                publishedAt = "ad",
                source = Source(id = null, name = null),
                url = "https://www.google.com/#q=orci",
                urlToImage = null
            ), Article(
                title = "Additional Test Article",
                author = null,
                content = null,
                description = null,
                publishedAt = "ad",
                source = Source(id = null, name = null),
                url = "https://www.google.com/#q=orci",
                urlToImage = null
            )
        )
        coEvery { newsApiService.getTopHeadlinesPage(1) } returns Response.success(
            TopHeadlinesResponse(
                additionalArticles, status = "his", totalResults = 5814
            )
        )

        val result = newsArticleRepository.fetchAdditionalTopHeadlines()

        assertEquals(NewsArticleResult.Success, result)
        assertEquals(
            additionalArticles, newsArticleRepository.articles.value
        )
    }
}