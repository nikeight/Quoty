package com.appchefs.quoty.main.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.appchefs.quoty.R
import com.appchefs.quoty.data.remote.NetworkService
import com.appchefs.quoty.data.remote.Networking
import com.appchefs.quoty.data.repo.QuoteRepository
import com.appchefs.quoty.databinding.ActivityMainBinding
import com.appchefs.quoty.main.base.BaseActivity
import com.appchefs.quoty.main.viewmodel.MainViewModel
import com.appchefs.quoty.main.viewmodel.ResponseViewModel
import com.appchefs.quoty.utils.NetworkUtils
import com.appchefs.quoty.utils.Resource
import com.appchefs.quoty.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity<ResponseViewModel, ActivityMainBinding>() {

    @Inject
    lateinit var quoteRepository: QuoteRepository

    override val mViewModel: ResponseViewModel by viewModels()
    private var currentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        clickEvents()

        GlobalScope.launch {
            quoteRepository.getRandomQuote().collect { resources ->
                when(resources){
                    is Resource.Success -> {
                        Log.d("ResponseQuoteDAO",resources.data.id)
                    }
                    is Resource.Failed -> {
                        Log.d("ResponseQuoteDAO",resources.message)
                    }
                }
            }
        }
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onStart() {
        super.onStart()
        getRandomQuoteObserver()
        getQuoteObserver()
        networkCheck()
    }

    private fun networkCheck() {
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this) { isConnected ->
            if (!isConnected) {
                mViewBinding.textViewNetworkStatus.text = "No Connections"
                mViewBinding.networkStatusLayout.apply {
                    show()
                    setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.networkNotAvailable
                        )
                    )
                }
            } else {
                // TODO: Implement the State of the LiveData
                mViewBinding.textViewNetworkStatus.text = "Back Online"
                mViewBinding.networkStatusLayout.apply {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.networkConnected
                        )
                    )

                    animate()
                        .alpha(1f)
                        .setStartDelay(1000L)
                        .setDuration(1000L)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hide()
                            }
                        })
                }
            }
        }
    }

    private fun clickEvents() {
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
            when (currentTag) {
                "random" -> mViewModel.getRandomQuote()
                "wisdom" -> mViewModel.getQuoteByTag("wisdom")
                "life" -> mViewModel.getQuoteByTag("life")
                "technology" -> mViewModel.getQuoteByTag("technology")
                else -> mViewModel.getRandomQuote()
            }
        }

    }

    private fun getRandomQuoteObserver() {
        mViewModel.randomQuote.observe(this, Observer { state ->
            when (state) {
                is Status.Error ->{
                    showToast(state.message)
                }
                is Status.Success ->{
                    mViewBinding.tvQuote.text = state.data.content
                    mViewBinding.tvAuthor.text = state.data.author
                }
                else -> {
                    showToast("Loading")
                }
            }
        })
    }

    private fun getQuoteObserver() {
        mViewModel.quote.observe(this, Observer { state ->
            when (state) {
                is Status.Error -> {
                    showToast(state.message)
                }
                is Status.Success ->{
                    mViewBinding.tvQuote.text = state.data.content
                    mViewBinding.tvAuthor.text = state.data.author
                }
                else -> {
                    showToast("Loading")
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.theme_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.theme_menu_icon -> {
                // Get new mode.
                val mode =
                    if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_NO
                    ) {
                        // Dark Theme by default
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        // uses dark theme when the Phone is in Battery Saver mode.
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    }

                // Change UI Mode
                AppCompatDelegate.setDefaultNightMode(mode)
                //TODO: Change the Icon.
                true
            }

            else -> true
        }
    }


    fun View.show() {
        visibility = View.VISIBLE
    }

    fun View.hide() {
        visibility = View.GONE
    }

    fun showToast(msg: String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }
}