package com.hrmoller.newsapp.hiltmodules

import com.hrmoller.newsapp.repositories.NewsArticleRepository
import com.hrmoller.newsapp.viewmodels.NewsArticleListViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideNewsArticleListViewModel(newsArticleRepository: NewsArticleRepository): NewsArticleListViewModel {
        return NewsArticleListViewModel(newsArticleRepository)
    }
}