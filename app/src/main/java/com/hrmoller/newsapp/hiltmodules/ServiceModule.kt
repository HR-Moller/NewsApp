package com.hrmoller.newsapp.hiltmodules

import com.hrmoller.newsapp.repositories.NewsArticleRepository
import com.hrmoller.newsapp.repositories.impl.NewsArticleRepositoryImpl
import com.hrmoller.newsapp.services.NewsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun provideNewsArticleRepository(apiService: NewsApiService): NewsArticleRepository {
        return NewsArticleRepositoryImpl(apiService)
    }
}