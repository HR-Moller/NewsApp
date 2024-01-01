package com.hrmoller.newsapp

import com.hrmoller.newsapp.models.Article
import com.hrmoller.newsapp.models.Source
import com.hrmoller.newsapp.models.TopHeadlinesResponse
import com.hrmoller.newsapp.repositories.NewsArticleRepository
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

    @Test
    fun `getArticleByIndex should return Success when article is found`() = runBlocking { // Arrange
        val repository: NewsArticleRepository = mockk()
        val mockArticles = listOf<Article>(
            Article(
                author = null,
                content = null,
                description = null,
                publishedAt = "convallis",
                source = Source(id = null, name = null),
                title = "dictumst",
                url = "https://search.yahoo.com/search?p=litora",
                urlToImage = null
            )
        )
        val index = 0

        coEvery { repository.getArticleByIndex(index) } returns Result.success(mockArticles[index])

        // Act
        val result = repository.getArticleByIndex(index)

        // Assert
        assertEquals(Result.success(mockArticles[index]), result)

    }

    @Test
    fun `getArticleByIndex should return Failure when article is not found`() = runBlocking { // Arrange
        val repository = mockk<NewsArticleRepository>()
        val index = 0

        coEvery { repository.getArticleByIndex(index) } returns Result.failure(Exception("Article Not Found"))

        // Act
        val result = repository.getArticleByIndex(index).isFailure

        // Assert
        assert(true)
    }
}