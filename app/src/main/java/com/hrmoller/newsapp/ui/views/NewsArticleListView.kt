@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.hrmoller.newsapp.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hrmoller.newsapp.R
import com.hrmoller.newsapp.models.Article
import com.hrmoller.newsapp.models.Source
import com.hrmoller.newsapp.ui.LoadingIndicator
import com.hrmoller.newsapp.ui.theme.Typography
import com.hrmoller.newsapp.viewmodels.NewsArticleListState
import com.hrmoller.newsapp.viewmodels.NewsArticleListViewModel
import java.text.SimpleDateFormat

@Composable
fun NewsArticleListView(
    viewModel: NewsArticleListViewModel = hiltViewModel(),
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(refreshing = state.isRefreshing, onRefresh = viewModel::onRefresh)

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) {
        NewsArticleListViewContent(
            state = state,
            snackbarHostState = snackbarHostState,
            listState = listState,
            pullRefreshState = pullRefreshState,
            padding = it,
            navController = navController,
            onEndReached = viewModel::onEndReached
        )
    }
}

@Composable
private fun NewsArticleListViewContent(
    state: NewsArticleListState,
    snackbarHostState: SnackbarHostState,
    listState: LazyListState,
    padding: PaddingValues,
    pullRefreshState: PullRefreshState,
    navController: NavHostController,
    onEndReached: () -> Unit
) {
    if (state.isLoading) LoadingIndicator()
    when {
        state.data.isNotEmpty() ->

            Box() {
                NewsArticleList(
                    articles = state.data,
                    listState,
                    navController = navController,
                    pullRefreshState = pullRefreshState,
                    onEndReached = onEndReached
                )

                PullRefreshIndicator(
                    refreshing = state.isLoading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    backgroundColor = if (state.isRefreshing) Color.Red else Color.Green,
                )
            }
    }
    if (state.error) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(message = state.errorMessage.orEmpty())
        }
    }

}


@Composable
private fun NewsArticleList(
    articles: List<Article>,
    listState: LazyListState,
    pullRefreshState: PullRefreshState,
    navController: NavHostController,
    onEndReached: () -> Unit
) {
    LazyColumn(
        state = listState, modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        items(articles) {
            ArticleListItem(article = it, articles.indexOf(it), navController = navController)
            if (articles.indexOf(it) == articles.count() - 1) {
                LaunchedEffect(key1 = articles.count()) {
                    onEndReached()
                }
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
private fun ArticleListItem(article: Article, index: Int, navController: NavHostController) {
    ListItem(modifier = Modifier
        .padding(16.dp, 4.dp)
        .shadow(8.dp, shape = RoundedCornerShape(8.dp))
        .clip(
            RoundedCornerShape(8.dp)
        )
        .clickable { navController.navigate("ArticleDetail/$index") }, headlineContent = {
        ItemTitle(title = article.title)
    }, supportingContent = {
        Column {
            ItemDescription(description = article.description.orEmpty())
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            ItemDate(date = format.parse(article.publishedAt)?.toString() ?: article.publishedAt)
        }
    }, leadingContent = {
        ItemImage(url = article.urlToImage.orEmpty())
    })
}

@Composable
fun ItemImage(url: String) {

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(url).crossfade(true).build(),
        contentDescription = "Article Image",
        error = painterResource(R.drawable.istockphoto_1147544807_612x612),
        fallback = painterResource(R.drawable.istockphoto_1147544807_612x612),
        placeholder = painterResource(R.drawable.istockphoto_1147544807_612x612),
        contentScale = ContentScale.Inside,
        modifier = Modifier
            .fillMaxSize(0.3F)
            .wrapContentHeight(Alignment.CenterVertically)
    )
}

@Composable
private fun ItemTitle(title: String) {
    Text(text = title, style = Typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
}

@Composable
private fun ItemDate(date: String) {
    Text(text = date)
}

@Composable
private fun ItemDescription(description: String) {
    Text(text = description, maxLines = 1, overflow = TextOverflow.Ellipsis)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewsArticleListPreview() {
    val refreshState = rememberPullRefreshState(refreshing = false, onRefresh = { })

    NewsArticleList(articles = listOf(
        Article(
            author = null,
            content = null,
            description = "purus",
            publishedAt = "vestibulum",
            source = Source(id = null, name = null),
            title = "suas",
            url = "https://www.google.com/#q=dolorem",
            urlToImage = null
        ), Article(
            author = null,
            content = null,
            description = "definiebas",
            publishedAt = "vis",
            source = Source(id = null, name = null),
            title = "consequat",
            url = "https://duckduckgo.com/?q=sonet",
            urlToImage = null
        )
    ),
        LazyListState(),
        navController = NavHostController(LocalContext.current),
        pullRefreshState = refreshState,
        onEndReached = {})
}