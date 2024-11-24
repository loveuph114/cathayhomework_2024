package cc.reece.cathayhomework_2024.network

import cc.reece.cathayhomework_2024.model.AttractionResult
import cc.reece.cathayhomework_2024.model.NewsResult
import cc.reece.cathayhomework_2024.model.RequestResult

class TravelTaipeiRepository(
    private val travelTaipeiApiService: TravelTaipeiApiService
) {

    suspend fun fetchAttractions(lang: String, page: Int): RequestResult<AttractionResult> {
        return safeApiCall {
            travelTaipeiApiService.fetchAttractions(lang, page)
        }
    }

    suspend fun fetchNews(lang: String, page: Int): RequestResult<NewsResult> {
        return safeApiCall {
            travelTaipeiApiService.fetchNews(lang, page)
        }
    }

}