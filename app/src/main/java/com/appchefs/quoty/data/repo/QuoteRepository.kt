package com.appchefs.quoty.data.repo

import com.appchefs.quoty.data.remote.NetworkService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteRepository @Inject constructor(
    private val networkService: NetworkService
) {

    fun getRandomQuotes() = networkService.getRandomQuotes()

    fun getQuotesByTag(tags : String) = networkService.getQuote(tags)
}