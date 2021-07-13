package com.appchefs.quoty.di.module

import com.appchefs.quoty.data.remote.NetworkService
import com.appchefs.quoty.data.remote.Networking
import com.appchefs.quoty.data.repo.QuoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class QuoteApiModule {

    @Singleton
    @Provides
    fun provideNetworkService() : NetworkService = Networking.create()

}