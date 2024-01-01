package com.hrmoller.newsapp.ui.views

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController

@Composable
fun ArticleWebView(url: String, navController: NavHostController) {

    Column {
        BackToDetailButton {
            navController.popBackStack()
        }
        AndroidView(factory = { context ->
            WebView(context).apply {
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                webChromeClient = WebChromeClient()
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                settings.safeBrowsingEnabled = true
            }
        }, update = { webView ->
            webView.loadUrl(url)
        }, modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun BackToDetailButton(onclick: () -> Unit) {
    IconButton(onClick = { onclick() }, modifier = Modifier
        .zIndex(1.00.toFloat())
        .requiredSize(48.dp)) {
        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back to article")
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ArticleWebViewPreview() {
    ArticleWebView(url = "", navController = NavHostController(LocalContext.current))
}