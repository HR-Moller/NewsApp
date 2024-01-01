package com.hrmoller.newsapp.repositories.result

sealed class NewsArticleResult {
    data class Error(val message: String = "An unknown error occurred, try again later") : NewsArticleResult()
    object Success : NewsArticleResult()
}
