package com.appchefs.quoty.main.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.appchefs.quoty.data.remote.NetworkService
import com.appchefs.quoty.data.remote.Networking
import com.appchefs.quoty.databinding.ActivityMainBinding
import com.appchefs.quoty.main.base.BaseActivity
import com.appchefs.quoty.main.viewmodel.MainViewModel
import com.appchefs.quoty.main.viewmodel.ResponseViewModel
import com.appchefs.quoty.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity<ResponseViewModel, ActivityMainBinding>() {

    override val mViewModel: ResponseViewModel by viewModels()
    private var currentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        clickEvents()
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onStart() {
        super.onStart()
        getRandomQuoteObserver()
        getQuoteObserver()
    }

    private fun clickEvents(){
        mViewBinding.btnRandom.setOnClickListener {
            currentTag = "random"
            mViewModel.getRandomQuote()
        }

        mViewBinding.btnWisdom.setOnClickListener {
            currentTag = "wisdom"
            mViewModel.getQuoteByTag("wisdom")
        }


        mViewBinding.btnLife.setOnClickListener {
            currentTag = "life"
            mViewModel.getQuoteByTag("life")
        }


        mViewBinding.btnTech.setOnClickListener {
            currentTag = "technology"
            mViewModel.getQuoteByTag("technology")
        }

        mViewBinding.fabNewQuote.setOnClickListener {
            when(currentTag){
                "random" -> mViewModel.getRandomQuote()
                "wisdom" -> mViewModel.getQuoteByTag("wisdom")
                "life" -> mViewModel.getQuoteByTag("life")
                "technology" -> mViewModel.getQuoteByTag("technology")
                else -> mViewModel.getRandomQuote()
            }
        }

    }

    private fun getRandomQuoteObserver(){
        mViewModel.randomQuote.observe(this, Observer {quote ->
            if (quote != null){
                mViewBinding.tvQuote.text = quote.content
                mViewBinding.tvAuthor.text = quote.author
            }else{
                mViewBinding.tvQuote.text = "Loading"
                mViewBinding.tvAuthor.text = "Loading"
            }
        })
    }

    private fun getQuoteObserver(){
        mViewModel.quote.observe(this, Observer {quote ->
            if (quote != null){
                mViewBinding.tvQuote.text = quote.content
                mViewBinding.tvAuthor.text = quote.author
            }else{
                mViewBinding.tvQuote.text = "Loading"
                mViewBinding.tvAuthor.text = "Loading"
            }
        })
    }
}