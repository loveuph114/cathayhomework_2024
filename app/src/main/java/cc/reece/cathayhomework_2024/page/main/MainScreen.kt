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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import cc.reece.cathayhomework_2024.R
import cc.reece.cathayhomework_2024.model.Attraction
import cc.reece.cathayhomework_2024.model.MainResult
import cc.reece.cathayhomework_2024.model.News
import cc.reece.cathayhomework_2024.model.UiState
import cc.reece.cathayhomework_2024.model.lang.Language
import cc.reece.cathayhomework_2024.network.RetrofitClient
import cc.reece.cathayhomework_2024.network.TravelTaipeiRepository
import cc.reece.cathayhomework_2024.ui.components.CathayScaffold
import cc.reece.cathayhomework_2024.ui.components.CathayTopAppBar
import cc.reece.cathayhomework_2024.ui.components.LanguageSelectorBottomSheet
import cc.reece.cathayhomework_2024.ui.theme.CathayTheme
import cc.reece.cathayhomework_2024.utils.AppPrefs
import cc.reece.cathayhomework_2024.utils.getAppPrefs
import cc.reece.cathayhomework_2024.utils.withAlpha
import coil.compose.AsyncImage
import java.util.Locale

sealed interface MainScreenUiAction {
    data object Refresh : MainScreenUiAction
    data object LoadMore : MainScreenUiAction
    data object ToggleLanguageSheet : MainScreenUiAction
    data class SelectLanguage(val language: Language) : MainScreenUiAction
    data class NewsClick(val news: News) : MainScreenUiAction
    data class AttractionClick(val attraction: Attraction) : MainScreenUiAction
}

private class MainViewModelFactory(
    private val repository: TravelTaipeiRepository,
    private val appPrefs: AppPrefs
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(repository, appPrefs) as T
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(
            TravelTaipeiRepository(RetrofitClient.travelTaipeiApiService),
            LocalContext.current.getAppPrefs()
        )
    ),
    onAction: (MainScreenUiAction) -> Unit
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
            onAction = { action ->
                when (action) {
                    MainScreenUiAction.Refresh -> viewModel.refresh()
                    MainScreenUiAction.LoadMore -> viewModel.loadMoreAttractions()
                    MainScreenUiAction.ToggleLanguageSheet -> showBottomSheet = !showBottomSheet
                    is MainScreenUiAction.SelectLanguage -> {
                        showBottomSheet = false
                        viewModel.updateLanguage(action.language)
                        viewModel.refresh()
                    }

                    else -> onAction(action)
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
    onAction: (MainScreenUiAction) -> Unit
) {
    CathayScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topAppBar = {
            CathayTopAppBar(
                title = stringResource(id = R.string.app_name),
                actions = {
                    LanguageButton(onClick = { onAction(MainScreenUiAction.ToggleLanguageSheet) })
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        if (showBottomSheet) {
            LanguageSelectorBottomSheet(
                context = context,
                onDismissRequest = { onAction(MainScreenUiAction.ToggleLanguageSheet) },
                onLanguageSelected = { language ->
                    onAction(MainScreenUiAction.ToggleLanguageSheet)
                    onAction(MainScreenUiAction.SelectLanguage(language))
                    onAction(MainScreenUiAction.Refresh)
                }
            )
        }

        MainContent(
            uiState = uiState,
            loadMoreState = loadMoreState,
            onAction = onAction
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
    onAction: (MainScreenUiAction) -> Unit,
) {
    val isRefreshing = uiState is UiState.Loading
    val refreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { onAction(MainScreenUiAction.Refresh) },
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
                onRetry = { onAction(MainScreenUiAction.Refresh) }
            )

            is UiState.Success -> SuccessContent(
                news = uiState.result.news,
                attractions = uiState.result.attractions,
                loadMoreState = loadMoreState,
                onAction = onAction
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
            onClick = onRetry
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
    onAction: (MainScreenUiAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
        ),
    ) {
        newsSection(
            newsList = news,
            onClick = { onAction(MainScreenUiAction.NewsClick(it)) }
        )
        spacerItem(32.dp)
        attractionsSection(
            attractions = attractions,
            onLoadMore = { onAction(MainScreenUiAction.LoadMore) },
            onAttractionClick = { onAction(MainScreenUiAction.AttractionClick(it)) }
        )
        showLoadMoreIfNeed(loadMoreState)
    }
}


@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.newsSection(
    newsList: List<News>,
    onClick: (News) -> Unit
) {
    stickyHeader {
        Section(stringResource(id = R.string.news_title))
    }

    if (newsList.isNotEmpty()) {
        itemsIndexed(newsList) { index, news ->
            NewsItem(
                news = news,
                topRoundedCorner = index == 0,
                bottomRoundedCorner = index == newsList.size - 1,
                onClick = onClick
            )
            if (index < newsList.size) {
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    } else {
        noNewsItem()
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
    onAttractionClick: (Attraction) -> Unit
) {
    stickyHeader {
        Section(stringResource(id = R.string.attractions_title))
    }
    itemsIndexed(attractions) { index, item ->
        AttractionItem(
            attraction = item,
            onAttractionClick = onAttractionClick
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
    news: News,
    topRoundedCorner: Boolean = false,
    bottomRoundedCorner: Boolean = false,
    onClick: (News) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(
                RoundedCornerShape(
                    topStart = if (topRoundedCorner) 12.dp else 0.dp,
                    topEnd = if (topRoundedCorner) 12.dp else 0.dp,
                    bottomStart = if (bottomRoundedCorner) 12.dp else 0.dp,
                    bottomEnd = if (bottomRoundedCorner) 12.dp else 0.dp
                )
            )
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick(news) }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = news.title,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = news.postedDateOnly.toString(),
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun AttractionItem(
    attraction: Attraction,
    onAttractionClick: (Attraction) -> Unit
) {
    val name = attraction.name
    val address = attraction.address
    val category = attraction.category.joinToString(" · ") { it.name }
    val image = attraction.images.firstOrNull()?.src

    Box(
        modifier = Modifier
            .aspectRatio(3f / 2f)
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onAttractionClick(attraction) }
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