package cc.reece.cathayhomework_2024

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cc.reece.cathayhomework_2024.ui.components.CathayScaffold
import cc.reece.cathayhomework_2024.ui.components.CathayTopAppBar
import cc.reece.cathayhomework_2024.ui.theme.CathayTheme
import cc.reece.cathayhomework_2024.utils.withAlpha

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    CathayTheme {
        CathayScaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topAppBar = {
                CathayTopAppBar(
                    title = stringResource(id = R.string.app_name),
                    actions = {
                        LanguageButton(
                            onClick = {}
                        )
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            content = {
                MainContent()
            }
        )
    }
}

@Composable
fun LanguageButton(
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_language),
            contentDescription = "language settings",
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainContent() {
    val news = List(100) { "news $it" }.take(3)
    val items = List(20) { "item $it" }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
        ),
    ) {
        stickyHeader {
            Section("最新消息")
        }
        itemsIndexed(news) { index, item ->
            NewsItem(
                isFirst = index == 0,
                isLast = false
//                isLast = index == news.size - 1
            )
            if (index < news.size) {
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "顯示全部",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
        stickyHeader {
            Section("景點")
        }
        itemsIndexed(items) { index, item ->
            AttractionsItem()
        }
    }
}

@Composable
fun Section(
    title: String
) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(vertical = 8.dp, horizontal = 16.dp),
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun NewsItem(
    isFirst: Boolean = false,
    isLast: Boolean = false
) {
    val shape = when {
        isFirst -> RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        )

        isLast -> RoundedCornerShape(
            bottomStart = 12.dp,
            bottomEnd = 12.dp
        )

        else -> RectangleShape
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "臺北旅遊網Open API正式上線！讓旅遊網豐富的資料 產生全新的加值應用",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = "2020-10-26",
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun AttractionsItem() {
    Box(
        modifier = Modifier
            .aspectRatio(3f / 2f)
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                MaterialTheme.colorScheme.tertiary
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface.withAlpha(0.5f)
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                text = "龍山文創基地",
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "108 臺北市萬華區西園路一段145號B2 龍山寺地下街B2(龍山寺捷運站1號出口)",
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "藝文館所 · 歷史建築無 · 障礙旅遊推薦景點",
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsViewPreview() {
    NewsItem()
}

@Preview(showBackground = true)
@Composable
fun AttractionsViewPreview() {
    AttractionsItem()
}