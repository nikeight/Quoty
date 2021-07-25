package com.appchefs.quoty.di.module

import com.appchefs.quoty.data.local.dao.QuoteDao
import com.appchefs.quoty.data.repo.LocalRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@InstallIn(ActivityRetainedComponent::class)
@Module
class LocalRepoModule {

    @Provides
    fun providesLocalRepo(quoteDao: QuoteDao) = LocalRepo(quoteDao)
}