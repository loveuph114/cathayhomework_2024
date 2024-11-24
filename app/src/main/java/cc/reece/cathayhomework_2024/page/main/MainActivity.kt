package cc.reece.cathayhomework_2024.page.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cc.reece.cathayhomework_2024.page.attraction.AttractionActivity
import cc.reece.cathayhomework_2024.page.news.NewsActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen {
                when (it) {
                    is MainScreenUiAction.NewsClick -> {
                        startActivity(
                            NewsActivity.createIntent(
                                context = this,
                                url = it.news.url
                            )
                        )
                    }

                    is MainScreenUiAction.AttractionClick -> {
                        startActivity(
                            AttractionActivity.createIntent(
                                context = this,
                                attraction = it.attraction
                            )
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}
