package com.appchefs.quoty.data.repo

import com.appchefs.quoty.data.local.dao.QuoteDao
import com.appchefs.quoty.data.model.Quote
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LocalRepo @Inject constructor(
    private val quoteDao: QuoteDao
) {

    suspend fun getAllQuotesFromDb(): List<Quote> {
        return quoteDao.getAllQuotes()
    }

    suspend fun deleteQuoteFromDb(quote: Quote){
        return quoteDao.delete(quote)
    }

    suspend fun updateQuote(quote: Quote){
        return quoteDao.update(quote)
    }
}