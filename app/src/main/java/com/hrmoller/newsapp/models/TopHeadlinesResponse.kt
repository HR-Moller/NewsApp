package com.hrmoller.newsapp.models

//@JsonClass(generateAdapter = true)
data class TopHeadlinesResponse(
    val articles: List<Article>, val status: String, val totalResults: Int
)