package com.hrmoller.newsapp.viewmodels

import androidx.lifecycle.viewModelScope
import com.hrmoller.newsapp.models.Article
import com.hrmoller.newsapp.repositories.NewsArticleRepository
import com.hrmoller.newsapp.repositories.result.NewsArticleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsArticleListViewModel @Inject constructor(private val newsArticleRepository: NewsArticleRepository) :
        StateViewModel<NewsArticleListState>(NewsArticleListState.initial()) {

    init {
        viewModelScope.launch {

            when (val result = newsArticleRepository.fetchTopHeadlines()) {
                is NewsArticleResult.Error -> _state.value = _state.value.copy(
                    isLoading = false, data = emptyList(), error = true, errorMessage = result.message
                )

                is NewsArticleResult.Success -> _state.value = _state.value.copy(isLoading = false)
            }
            newsArticleRepository.articles.collect { value ->
                _state.value = _state.value.copy(data = value.filter { it.title != "[Removed]" })
            }
        }
    }

    fun onEndReached() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = newsArticleRepository.fetchAdditionalTopHeadlines()) {
                is NewsArticleResult.Error -> _state.value = _state.value.copy(
                    isLoading = false, error = true, errorMessage = result.message
                )

                NewsArticleResult.Success -> _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

}

data class NewsArticleListState(
    val isLoading: Boolean, val data: List<Article>, val error: Boolean, val errorMessage: String?
) : UiState {
    companion object {
        fun initial() = NewsArticleListState(isLoading = true, data = emptyList(), error = false, errorMessage = null)
    }
}
