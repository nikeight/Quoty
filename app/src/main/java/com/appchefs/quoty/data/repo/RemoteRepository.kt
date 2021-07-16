package com.appchefs.quoty.data.repo

import com.appchefs.quoty.data.remote.NetworkService
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val networkService: NetworkService
) {

    suspend fun getRandomApiResponse() = networkService.getRandomQuotesAPIResponse()

    suspend fun getQuoteByTagResponse(tag: String) = networkService.getRandomQuotesAPIResponse(tag)
}