package com.appchefs.quoty.data.remote

import com.appchefs.quoty.data.model.Quote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {

    // Todo: Correct the query part methods.

    @GET(Endpoints.RANDOM)
    suspend fun getRandomQuotes(): Response<Quote>

    @GET(Endpoints.RANDOM)
    suspend fun getQuote(
        @Query("tags") tags: String
    ) : Response<Quote>
}