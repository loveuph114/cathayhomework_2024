package cc.reece.cathayhomework_2024.network

import cc.reece.cathayhomework_2024.model.AttractionResult
import cc.reece.cathayhomework_2024.model.NewsResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TravelTaipeiApiService {

    @GET("{lang}/Attractions/All")
    suspend fun fetchAttractions(
        @Path("lang") lang: String,
        @Query("page") page: Int,
    ): AttractionResult

    @GET("{lang}/Events/News")
    suspend fun fetchNews(
        @Path("lang") lang: String,
        @Query("page") page: Int,
    ): NewsResult

}

