package com.appchefs.quoty.data.remote

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Networking {

    private val BASE_URL = "https://api.quotable.io/"

    fun create(): NetworkService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build()
            )
            .build()
            .create(NetworkService::class.java)
    }
}