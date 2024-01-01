package com.hrmoller.newsapp.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hrmoller.newsapp.ui.views.ArticleWebView
import com.hrmoller.newsapp.ui.views.NewsArticleDetailView
import com.hrmoller.newsapp.ui.views.NewsArticleListView

@Composable
fun NewsArticleNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    snackbarHostState: SnackbarHostState
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("ArticleList") {
            NewsArticleListView(navController = navController, snackbarHostState = snackbarHostState)
        }
        composable("ArticleDetail/{index}", arguments = listOf(navArgument("index") {
            type = NavType.IntType
        })) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index")
            NewsArticleDetailView(
                index = index ?: 1,
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
        composable("ArticleWebView/{url}", arguments = listOf(navArgument("url") {
            type = NavType.StringType
        })) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url")
            ArticleWebView(url = url ?: "", navController = navController)
        }
    }
}
