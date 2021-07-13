package com.appchefs.quoty.data.remote

import com.appchefs.quoty.data.model.Quote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @GET(Endpoints.RANDOM)
    fun getRandomQuotes(): Response<Quote>

    @GET()
    fun getQuote(
        @Query("tags") tags: String
    ) : Response<Quote>
}