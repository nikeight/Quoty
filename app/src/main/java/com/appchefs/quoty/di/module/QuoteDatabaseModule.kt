package com.appchefs.quoty.di.module

import android.app.Application
import com.appchefs.quoty.data.local.QuoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class QuoteDatabaseModule {

    // Todo: By using @ApplicationContext here. Instead of passing application only.
    @Singleton
    @Provides
    fun provideQuoteDatabase(application: Application) =
        QuoteDatabase.getInstance(application)

    @Singleton
    @Provides
    fun provideQuoteDao(database: QuoteDatabase) = database.getQuoteDao()
}