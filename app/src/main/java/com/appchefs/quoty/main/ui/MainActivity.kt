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
import com.appchefs.quoty.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val mViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
//        clickEvents()

        GlobalScope.launch(Dispatchers.IO) {
            val response = Networking.create().getRandomQuotes()
            if (response.isSuccessful){
                Log.d("Response",response.body()?.quoteContent.toString())
            }else{
                Log.d("Response","Failed API")
            }
        }
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onStart() {
        super.onStart()
//        getRandomQuoteObserver()
//        getQuoteObserver()
    }

    private fun clickEvents(){
        mViewBinding.btnRandom.setOnClickListener {
            mViewModel.getRandomQuote()
        }

        mViewBinding.btnWisdom.setOnClickListener {
            mViewModel.getQuote("wisdom")
        }


        mViewBinding.btnLife.setOnClickListener {
            mViewModel.getQuote("life")
        }


        mViewBinding.btnTech.setOnClickListener {
            mViewModel.getQuote("technology")
        }

    }

    private fun getRandomQuoteObserver(){
        mViewModel.randomQuote.observe(this, Observer {state ->
            when(state){
                is Status.Success -> {
                    mViewBinding.tvQuote.text = state.data.quoteContent
                    mViewBinding.tvAuthor.text = state.data.author
                }

                is Status.Error -> {
                    Toast.makeText(this,"Couldn't able to load the quotes",Toast.LENGTH_SHORT)
                        .show()
                }

                is Status.Loading -> {
                    Toast.makeText(this,"Loading",Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun getQuoteObserver(){
        mViewModel.quote.observe(this, Observer {state ->
            when(state){
                is Status.Success -> {
                    mViewBinding.tvQuote.text = state.data.quoteContent
                    mViewBinding.tvAuthor.text = state.data.author
                }

                is Status.Error -> {
                    Toast.makeText(this,"Couldn't able to load the quotes",Toast.LENGTH_SHORT)
                        .show()
                }

                is Status.Loading -> {
                    Toast.makeText(this,"Loading",Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}