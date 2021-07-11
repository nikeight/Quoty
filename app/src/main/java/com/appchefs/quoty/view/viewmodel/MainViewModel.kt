package com.appchefs.quoty.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appchefs.quoty.data.model.Quote
import com.appchefs.quoty.data.repo.QuoteRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val compositeDisposable: CompositeDisposable,
    private val quoteRepository: QuoteRepository
): ViewModel() {

    private val _randomQuote =  MutableLiveData<Quote>()
    val randomQuote : LiveData<Quote> = _randomQuote

    fun getRandomQuotes(){
        compositeDisposable.addAll(
            quoteRepository.getRandomQuotes()
                .subscribeOn(Schedulers.io())
                .subscribe({
                           _randomQuote.postValue(it)
                },{
                    //TODO: To handle the exceptions errors and internet check class
                })
        )
    }
}