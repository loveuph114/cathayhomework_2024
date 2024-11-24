package cc.reece.cathayhomework_2024.page.main

import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import cc.reece.cathayhomework_2024.R
import cc.reece.cathayhomework_2024.network.RetrofitClient
import cc.reece.cathayhomework_2024.network.TravelTaipeiRepository
import cc.reece.cathayhomework_2024.model.Attraction
import cc.reece.cathayhomework_2024.model.MainResult
import cc.reece.cathayhomework_2024.model.News
import cc.reece.cathayhomework_2024.model.UiState
import cc.reece.cathayhomework_2024.model.lang.Language
import cc.reece.cathayhomework_2024.ui.components.CathayScaffold
import cc.reece.cathayhomework_2024.ui.components.CathayTopAppBar
import cc.reece.cathayhomework_2024.ui.components.LanguageSelectorBottomSheet
import cc.reece.cathayhomework_2024.ui.theme.CathayTheme
import cc.reece.cathayhomework_2024.utils.withAlpha
import coil.compose.AsyncImage
import java.util.Locale

sealed interface MainScreenUiEvent {
    data object Refresh : MainScreenUiEvent
    data object LoadMore : MainScreenUiEvent
    data object ToggleLanguageSheet : MainScreenUiEvent
    data class SelectLanguage(val language: Language) : MainScreenUiEvent
}

private class MainViewModelFactory(
    private val repository: TravelTaipeiRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(repository) as T
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(
            TravelTaipeiRepository(RetrofitClient.travelTaipeiApiService)
        )
    )
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()
    val loadMoreState by viewModel.loadMoreState.collectAsState()
    val locale by viewModel.currentLocale.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val context = rememberLocalizedContext(locale)

    CathayTheme(context) {
        MainScreenContent(
            context = context,
            scrollBehavior = scrollBehavior,
            uiState = uiState,
            loadMoreState = loadMoreState,
            showBottomSheet = showBottomSheet,
            onEvent = { event ->
                when (event) {
                    MainScreenUiEvent.Refresh -> viewModel.refresh()
                    MainScreenUiEvent.LoadMore -> viewModel.loadMoreAttractions()
                    MainScreenUiEvent.ToggleLanguageSheet -> showBottomSheet = !showBottomSheet
                    is MainScreenUiEvent.SelectLanguage -> {
                        showBottomSheet = false
                        viewModel.updateLanguage(event.language)
                        viewModel.refresh()
                    }
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenContent(
    context: Context,
    scrollBehavior: TopAppBarScrollBehavior,
    uiState: UiState<MainResult>,
    loadMoreState: UiState<Unit>,
    showBottomSheet: Boolean,
    onEvent: (MainScreenUiEvent) -> Unit
) {
    CathayScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topAppBar = {
            CathayTopAppBar(
                title = stringResource(id = R.string.app_name),
                actions = {
                    LanguageButton(onClick = { onEvent(MainScreenUiEvent.ToggleLanguageSheet) })
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        if (showBottomSheet) {
            LanguageSelectorBottomSheet(
                context = context,
                onDismissRequest = { onEvent(MainScreenUiEvent.ToggleLanguageSheet) },
                onLanguageSelected = { language ->
                    onEvent(MainScreenUiEvent.ToggleLanguageSheet)
                    onEvent(MainScreenUiEvent.SelectLanguage(language))
                    onEvent(MainScreenUiEvent.Refresh)
                }
            )
        }

        MainContent(
            uiState = uiState,
            loadMoreState = loadMoreState,
            onRefresh = { onEvent(MainScreenUiEvent.Refresh) },
            onLoadMore = { onEvent(MainScreenUiEvent.LoadMore) }
        )
    }
}

@Composable
private fun rememberLocalizedContext(locale: Locale): Context {
    val currentContext = LocalContext.current
    return remember(locale) {
        val config = Configuration(currentContext.resources.configuration)
        config.setLocale(locale)
        currentContext.createConfigurationContext(config)
    }
}

@Composable
private fun LanguageButton(
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_language),
            contentDescription = "language settings",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    uiState: UiState<MainResult>,
    loadMoreState: UiState<Unit>,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
) {
    val isRefreshing = uiState is UiState.Loading
    val refreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize(),
        state = refreshState,
        indicator = {
            PullToRefreshDefaults.Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                state = refreshState,
                containerColor = MaterialTheme.colorScheme.surface,
                color = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        when (uiState) {
            is UiState.Error -> ErrorContent(
                throwable = uiState.throwable,
                onRetry = { onRefresh() }
            )

            is UiState.Success -> SuccessContent(
                news = uiState.result.news.take(3),
                attractions = uiState.result.attractions,
                loadMoreState = loadMoreState,
                onLoadMore = onLoadMore
            )

            UiState.Loading -> {}
        }
    }
}

@Composable
private fun ErrorContent(
    throwable: Throwable,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Oops!\n${throwable.localizedMessage.orEmpty()}",
            modifier = Modifier
                .padding(top = 160.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
        TextButton(
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally),
            onClick = { onRetry() }
        ) {
            Text("Retry")
        }
    }
}

@Composable
private fun SuccessContent(
    news: List<News>,
    attractions: List<Attraction>,
    loadMoreState: UiState<Unit>,
    onLoadMore: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
        ),
    ) {
        newsSection(news)
        spacerItem(32.dp)
        attractionsSection(attractions, onLoadMore)
        showLoadMoreIfNeed(loadMoreState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.newsSection(news: List<News>) {
    stickyHeader {
        Section(stringResource(id = R.string.news_title))
    }

    if (news.isNotEmpty()) {
        itemsIndexed(news) { index, item ->
            NewsItem(
                title = item.title,
                posted = item.postedDateOnly.toString(),
                topRoundedCorner = index == 0
            )
            if (index < news.size) {
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
        showAllNewsButton()
    } else {
        noNewsItem()
    }
}

private fun LazyListScope.showAllNewsButton() {
    item {
        ShowAllButton(onClick = { })
    }
}

private fun LazyListScope.noNewsItem() {
    item {
        NoNewsItem()
    }
}

private fun LazyListScope.spacerItem(height: Dp) {
    item {
        Spacer(modifier = Modifier.height(height))
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.attractionsSection(
    attractions: List<Attraction>,
    onLoadMore: () -> Unit,
) {
    stickyHeader {
        Section(stringResource(id = R.string.attractions_title))
    }
    itemsIndexed(attractions) { index, item ->
        AttractionItem(
            name = item.name,
            address = item.address,
            category = item.category.joinToString(" · ") { it.name },
            image = item.images.firstOrNull()?.src
        )
        LaunchedEffect(attractions.size) {
            if (attractions.size - index < 2) {
                onLoadMore()
            }
        }
    }
}

private fun LazyListScope.showLoadMoreIfNeed(
    loadMoreState: UiState<Unit>,
) {
    item {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            when (loadMoreState) {
                is UiState.Error -> {
                    Text(text = loadMoreState.throwable.message.orEmpty())
                }

                UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp
                    )
                }

                is UiState.Success -> {}
            }
        }
    }
}

@Composable
private fun ShowAllButton(
    onClick: () -> Unit
) {
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
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.show_all),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun NoNewsItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "NO NEWS",
            style = MaterialTheme.typography.bodySmall,
        )
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
    title: String,
    posted: String,
    topRoundedCorner: Boolean = false,
) {
    val shape = when {
        topRoundedCorner -> RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
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
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = posted,
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun AttractionItem(
    name: String,
    address: String,
    category: String,
    image: String?
) {
    Box(
        modifier = Modifier
            .aspectRatio(3f / 2f)
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { }
            .background(MaterialTheme.colorScheme.tertiary),
        contentAlignment = Alignment.BottomCenter
    ) {
        if (image == null) {
            Text(
                text = "NO IMAGE",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
            )
        } else {
            AsyncImage(
                model = image,
                contentDescription = "attraction image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface.withAlpha(0.7f)
                )
                .padding(8.dp),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                text = name,
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = address,
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
            )
            if (category.isNotEmpty()) {
                Text(
                    text = category,
                    modifier = Modifier.padding(top = 4.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ErrorContentPreview() {
    ErrorContent(
        throwable = RuntimeException("HaHaHa"),
        onRetry = {}
    )
}

@Preview
@Composable
private fun NewsViewPreview() {
    NewsItem(
        title = "這個是最新消息的標題",
        posted = "2024-11-25"
    )
}

@Preview
@Composable
fun AttractionsViewPreview() {
    AttractionItem(
        name = "臺北盆地",
        address = "台灣的北部",
        category = "",
        image = "https://www.travel.taipei/image/222315"
    )
}