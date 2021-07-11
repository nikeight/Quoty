package com.appchefs.quoty.data.remote

import com.appchefs.quoty.data.model.Quote
import com.appchefs.quoty.data.remote.response.QuoteResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @GET(Endpoints.RANDOM)
    fun getRandomQuotes(): Single<Quote>

    @GET()
    fun getQuote(
        @Query("tags") tags: String
    ) : Single<Quote>
}