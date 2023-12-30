package com.hrmoller.newsapp.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hrmoller.newsapp.R
import com.hrmoller.newsapp.models.Article
import com.hrmoller.newsapp.models.Source
import com.hrmoller.newsapp.ui.LoadingIndicator
import com.hrmoller.newsapp.ui.theme.Typography
import com.hrmoller.newsapp.viewmodels.NewsArticleDetailState
import com.hrmoller.newsapp.viewmodels.NewsArticleDetailViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat

@Composable
fun NewsArticleDetailView(
    index: Int,
    viewModel: NewsArticleDetailViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) {
        ArticleDetailViewContent(state, snackbarHostState, it, navController)
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.onArticleClicked(index)
    }
}


@Composable
fun ArticleDetailViewContent(
    state: NewsArticleDetailState,
    snackbarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(paddingValues)
    ) {
        BackToListButton {
            navController.popBackStack()
        }
        if (state.isLoading) LoadingIndicator()
        if (state.error) {
            LaunchedEffect(snackbarHostState) {
                snackbarHostState.showSnackbar(message = state.errorMessage.orEmpty())
            }
        }
        if (!state.isLoading) {
            ArticleDetailViewItem(article = state.data, navController)
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun ArticleDetailViewItem(article: Article, navController: NavHostController) {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp, 48.dp, 8.dp, 0.dp)
    ) {
        ArticleImage(url = article.urlToImage.orEmpty())
        ArticleTitle(title = article.title)
        ArticleDescription(description = article.description.orEmpty())
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
        ArticleAuthor(author = article.author ?: "Unknown Author")
        ArticleDate(date = format.parse(article.publishedAt)?.toString() ?: article.publishedAt)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
        ArticleContent(content = article.content ?: "Unable to show content, try reading the full article online")
        ArticleURLBtn() {
            navController.navigate(
                "ArticleWebView/${
                    URLEncoder.encode(
                        article.url,
                        StandardCharsets.UTF_8.toString()
                    )
                }"
            )
        }
    }
}

@Composable
fun BackToListButton(onclick: () -> Unit) {
    IconButton(onClick = { onclick() }, modifier = Modifier.zIndex(1.00.toFloat())) {
        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back to list")
    }
}

@Composable
fun ArticleImage(url: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(url).crossfade(true).build(),
        contentDescription = "Article Image",
        fallback = painterResource(R.drawable.istockphoto_1147544807_612x612),
        error = painterResource(R.drawable.istockphoto_1147544807_612x612),
        placeholder = painterResource(R.drawable.istockphoto_1147544807_612x612),
        contentScale = ContentScale.FillWidth,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ArticleTitle(title: String) {
    Text(text = title, style = Typography.headlineMedium)
}

@Composable
fun ArticleContent(content: String) {
    Text(text = content, style = Typography.bodyMedium)
}

@Composable
fun ArticleDescription(description: String) {
    Text(text = description, style = Typography.titleMedium)
}

@Composable
fun ArticleDate(date: String) {
    Text(text = date, style = Typography.titleSmall, textAlign = TextAlign.End)
}

@Composable
fun ArticleAuthor(author: String) {
    Text(text = author, style = Typography.titleSmall, fontWeight = FontWeight.Bold)
}

@Composable
fun ArticleURLBtn(onclick: () -> Unit) {
    TextButton(onClick = { onclick() }) {
        Text(text = "Read full article online")
        Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Open WebView")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewsArticleDetailPreview() {
    ArticleDetailViewItem(
        article = Article(
            author = "Author",
            content = "Content",
            description = "Description",
            publishedAt = "2023-12-27T12:23:08Z",
            source = Source(id = "bbc", name = "BBC"),
            title = "Title",
            url = "https://www.google.com/#q=consequat",
            urlToImage = null
        ), navController = NavHostController(LocalContext.current)
    )
}