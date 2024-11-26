package cc.reece.cathayhomework_2024.page.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.reece.cathayhomework_2024.model.Attraction
import cc.reece.cathayhomework_2024.model.MainResult
import cc.reece.cathayhomework_2024.model.News
import cc.reece.cathayhomework_2024.model.RequestResult
import cc.reece.cathayhomework_2024.model.UiState
import cc.reece.cathayhomework_2024.model.lang.Language
import cc.reece.cathayhomework_2024.model.lang.supportedLanguages
import cc.reece.cathayhomework_2024.network.TravelTaipeiRepository
import cc.reece.cathayhomework_2024.utils.AppPrefs
import cc.reece.cathayhomework_2024.utils.LanguageHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MainViewModel(
    private val travelTaipeiRepository: TravelTaipeiRepository,
    private val appPrefs: AppPrefs
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<MainResult>>(UiState.Loading)
    val uiState: StateFlow<UiState<MainResult>> get() = _uiState

    private val _loadMoreState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val loadMoreState: StateFlow<UiState<Unit>> get() = _loadMoreState

    private val _currentLocale = MutableStateFlow(Locale.getDefault())
    val currentLocale: StateFlow<Locale> = _currentLocale

    private val newsCache = mutableListOf<News>()
    private val attractionsCache = mutableListOf<Attraction>()

    private var uiStateJob: Job? = null
    private var loadMoreAttractionsJob: Job? = null

    private lateinit var languageCode: String
    private var page = 1
    private var hasMoreData = true

    init {
        updateLanguage(supportedLanguages.first())
        fetch()
    }

    fun updateLanguage(language: Language) {
        language.code.apply {
            languageCode = this
            appPrefs.languageCode = this
            _currentLocale.value = LanguageHelper.getLocale(this)
        }
    }

    fun refresh() {
        newsCache.clear()
        attractionsCache.clear()
        uiStateJob?.cancel()
        loadMoreAttractionsJob?.cancel()
        page = 1
        fetch()
    }

    fun loadMoreAttractions() {
        if (loadMoreAttractionsJob?.isActive == true) {
            Log.d("REECE", "previous load more still active, cancel this one.")
            return
        }
        if (hasMoreData.not()) {
            Log.d("REECE", "no more data, no need to load more.")
            return
        }
        loadMoreAttractionsJob = viewModelScope.launch {
            _loadMoreState.value = UiState.Loading
            page++
            val attractionsResult =
                fetchAttractions(lang = languageCode, page = page).let {
                    when (it) {
                        is RequestResult.Error -> {
                            _loadMoreState.value = UiState.Error(it.throwable)
                            return@launch
                        }

                        is RequestResult.Success -> it.data
                    }
                }

            val attractions = attractionsResult.data
            hasMoreData = attractions.size >= 30
            attractionsCache.addAll(attractions)

            _loadMoreState.value = UiState.Success(Unit)
            _uiState.value = UiState.Success(
                MainResult(
                    news = newsCache.take(3),
                    attractions = attractionsCache
                )
            )
        }
    }

    private fun fetch() {
        _uiState.value = UiState.Loading
        uiStateJob = viewModelScope.launch {
            val newsDeferred = async { fetchNews() }
            val attractionsDeferred =
                async { fetchAttractions(lang = languageCode, page = page) }

            val newsResult = newsDeferred.await().let {
                when (it) {
                    is RequestResult.Error -> {
                        _uiState.value = UiState.Error(it.throwable)
                        return@launch
                    }

                    is RequestResult.Success -> it.data
                }
            }
            val attractionsResult = attractionsDeferred.await().let {
                when (it) {
                    is RequestResult.Error -> {
                        _uiState.value = UiState.Error(it.throwable)
                        return@launch
                    }

                    is RequestResult.Success -> it.data
                }
            }
            newsCache.addAll(newsResult.data)
            attractionsCache.addAll(attractionsResult.data)

            _uiState.value = UiState.Success(
                MainResult(
                    news = newsCache.take(3),
                    attractions = attractionsCache
                )
            )
        }
    }

    private suspend fun fetchNews() = withContext(Dispatchers.IO) {
        travelTaipeiRepository.fetchNews(lang = languageCode, page = 1)
    }

    private suspend fun fetchAttractions(lang: String, page: Int) = withContext(Dispatchers.IO) {
        travelTaipeiRepository.fetchAttractions(lang, page)
    }

}