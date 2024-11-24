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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
            NewsItem()
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary.withAlpha(alpha = 0.2f)
            )
        }
        item {
            TextButton(
                onClick = {},
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Text(
                    text = "顯示全部",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Spacer(modifier = Modifier.padding(12.dp))
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
        style = MaterialTheme.typography.labelLarge
    )
    HorizontalDivider(
        thickness = 3.dp,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun NewsItem() {
    Column(
        modifier = Modifier
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "臺北旅遊網Open API正式上線！讓旅遊網豐富的資料 產生全新的加值應用",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = "為提升臺北旅遊網的能見度以及達到政府開放資料目的，臺北市觀光傳播局於今（8）日啟動臺北旅遊網Open API（開放應用程式介面）功能，提供業者、民間社群以系統介接方式取得臺北旅遊網公開資料進行加值應用。",
            modifier = Modifier.padding(top = 4.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium,
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
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                MaterialTheme.colorScheme.tertiary
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary.withAlpha(0.5f)
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                text = "龍山文創基地",
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "108 臺北市萬華區西園路一段145號B2 龍山寺地下街B2(龍山寺捷運站1號出口)",
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "藝文館所 · 歷史建築無 · 障礙旅遊推薦景點",
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onPrimary,
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