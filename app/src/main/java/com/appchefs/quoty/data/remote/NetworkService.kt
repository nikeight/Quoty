package com.appchefs.quoty.data.remote

import com.appchefs.quoty.data.model.Quote
import com.appchefs.quoty.data.model.QuoteAPI
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {

    // Todo: Correct the query part methods.

    @GET("/random")
    suspend fun getRandomQuotes(): Response<Quote>

    @GET(Endpoints.RANDOM)
    fun getQuote(
        @Query("tags") tags: String
    ) : Response<Quote>

    // For the Network Response only
    @GET("/random")
    suspend fun getRandomQuotesAPIResponse(): Response<QuoteAPI>

    @GET(Endpoints.RANDOM)
    suspend fun getRandomQuotesAPIResponse(
        @Query("tags") tags: String
    ) : Response<QuoteAPI>
}