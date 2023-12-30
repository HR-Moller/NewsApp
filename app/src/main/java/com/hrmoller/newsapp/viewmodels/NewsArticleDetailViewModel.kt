package com.hrmoller.newsapp.viewmodels

import androidx.lifecycle.viewModelScope
import com.hrmoller.newsapp.models.Article
import com.hrmoller.newsapp.models.Source
import com.hrmoller.newsapp.repositories.NewsArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsArticleDetailViewModel @Inject constructor(private val newsArticleRepository: NewsArticleRepository) :
        StateViewModel<NewsArticleDetailState>(NewsArticleDetailState.initial()) {
    fun onArticleClicked(index: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            newsArticleRepository.getArticleByIndex(index).onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false, data = it
                )
            }.onFailure { _state.value = _state.value.copy(error = true, errorMessage = it.message) }
        }
    }

}

data class NewsArticleDetailState(
    val isLoading: Boolean, val data: Article, val error: Boolean, val errorMessage: String?
) : UiState {
    companion object {
        fun initial() = NewsArticleDetailState(
            isLoading = true, data = Article(
                author = null,
                content = null,
                description = null,
                publishedAt = "",
                source = Source(id = null, name = null),
                title = "",
                url = "",
                urlToImage = null
            ), error = false, errorMessage = null
        )
    }
}