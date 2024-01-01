package com.hrmoller.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hrmoller.newsapp.ui.NewsArticleNavHost
import com.hrmoller.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme { // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val snackbarHostState = remember { SnackbarHostState() }

                    Scaffold(snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }) {
                        Box(Modifier.padding(paddingValues = it)) {
                            NewsArticleNavHost(
                                navController = navController,
                                startDestination = "ArticleList",
                                snackbarHostState = snackbarHostState
                            )
                        }
                    }

                    /*val crashButton = Button(this)
                    crashButton.text = "Test Crash"
                    crashButton.setOnClickListener {
                        throw RuntimeException("Test Crash") // Force a crash
                    }

                    addContentView(crashButton, ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT))*/
                }
            }
        }
    }
}