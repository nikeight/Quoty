package com.appchefs.quoty.di.module

import com.appchefs.quoty.data.remote.NetworkService
import com.appchefs.quoty.data.remote.Networking
import com.appchefs.quoty.data.repo.QuoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class QuotyApiModule {

    @Singleton
    @Provides
    fun provideCompositeDisposable() : CompositeDisposable = CompositeDisposable()

    @Singleton
    @Provides
    fun provideNetworkService() : NetworkService = Networking.create()

    @Singleton
    @Provides
    fun provideQuoteRepository(networkService: NetworkService) : QuoteRepository =
        QuoteRepository(networkService)
}