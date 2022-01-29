package com.appchefs.quoty.main.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
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
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.appchefs.quoty.R
import com.appchefs.quoty.databinding.ActivityMainBinding
import com.appchefs.quoty.main.base.BaseActivity
import com.appchefs.quoty.main.viewmodel.MainViewModel
import com.appchefs.quoty.utils.NetworkUtils
import com.appchefs.quoty.utils.Status
import com.appchefs.quoty.worker.NotificationWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    private val TAG = "MainActivity"
    override val mViewModel: MainViewModel by viewModels()
    private var currentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        clickEvents()
        setupObservers()
        Log.i(TAG, "On created Called")
    }

    private fun startNotificationWorK() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicNotificationWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(12, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this)
            .enqueue(periodicNotificationWorkRequest)

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(periodicNotificationWorkRequest.id)
            .observe(this, { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    Log.i("WorkStatus", "Success")
                } else {
                    Log.i("WorkStatus", "Error")
                }
            })
    }


    private fun setupObservers() {
        getRandomQuoteObserver()
        getQuoteObserver()
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onStart() {
        super.onStart()
        networkCheck()
        startNotificationWorK()
    }

    override fun onResume() {
        super.onResume()
        loadRandomQuoteByDefault()
    }

    private fun loadRandomQuoteByDefault() {
        mViewBinding.btnToggleGroup.check(R.id.btn_random)
        mViewModel.getRandomQuote()
    }


    private fun clickEvents() {
        mViewBinding.btnRandom.setOnClickListener {
            currentTag = "random"
            mViewModel.getRandomQuote()
        }

        mViewBinding.btnWisdom.setOnClickListener {
            currentTag = "wisdom"
            mViewModel.getQuote("wisdom")
        }


        mViewBinding.btnLife.setOnClickListener {
            currentTag = "life"
            mViewModel.getQuote("life")
        }


        mViewBinding.btnTech.setOnClickListener {
            currentTag = "technology"
            mViewModel.getQuote("technology")
        }

        mViewBinding.fabNewQuote.setOnClickListener {
            when (currentTag) {
                "random" -> mViewModel.getRandomQuote()
                "wisdom" -> mViewModel.getQuote("wisdom")
                "life" -> mViewModel.getQuote("life")
                "technology" -> mViewModel.getQuote("technology")
                else -> mViewModel.getRandomQuote()
            }
        }

        mViewBinding.twitterImageView.setOnClickListener {
            twitterToggle(true)
            shareTweet("${mViewBinding.tvQuote.text} - ${mViewBinding.tvAuthor.text}")
        }

        mViewBinding.clipBoardImageView.setOnClickListener {
            val clipBoard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(
                "copied_quote",
                "${mViewBinding.tvQuote.text} ${mViewBinding.tvAuthor.text}"
            )
            clipBoard.setPrimaryClip(clipData)
            textCopiedState(true)
            showToast("Quote Copied!!")
        }
    }

    private fun shareTweet(message: String) {
        val tweetIntent = Intent(Intent.ACTION_SEND)
        tweetIntent.putExtra(Intent.EXTRA_TEXT,message)
        tweetIntent.type = "text/plain"

        val packageManager = packageManager
        val resolvedInfoList = packageManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY)
        var resolved = false
        for (resolveInfo in resolvedInfoList){
            // Todo Change the Package name of the Twitter here
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android.PostActivity")) {
                tweetIntent.setClassName(
                    resolveInfo.activityInfo.packageName,
                    resolveInfo.activityInfo.name
                )
                resolved = true
                break
            }
        }

        if (resolved){
            startActivity(tweetIntent)
        }else{
            val secondaryTweetIntent = Intent()
            secondaryTweetIntent.putExtra(Intent.EXTRA_TEXT,message)
            secondaryTweetIntent.action = Intent.ACTION_VIEW
            secondaryTweetIntent.data = Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message))
            startActivity(secondaryTweetIntent)
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show()
        }
    }

    private fun urlEncode(s: String): String? {
        return try {
            URLEncoder.encode(s, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            Log.wtf(TAG, "UTF-8 should always be supported", e)
            ""
        }
    }

    private fun twitterToggle(state: Boolean) {
        if (state)
            mViewBinding.twitterImageView.setImageResource(R.drawable.twitter_selected)
        else
            mViewBinding.twitterImageView.setImageResource(R.drawable.twitter_unselected)
    }

    private fun getRandomQuoteObserver() {
        mViewModel.randomQuote.observe(this, { state ->
            when (state) {
                is Status.Error -> {
                    showToast(state.message)
                }
                is Status.Success -> {
                    twitterToggle(false)
                    textCopiedState(false)
                    mViewBinding.tvQuote.text = state.data?.quoteContent ?: "Loading"
                    mViewBinding.tvAuthor.text = state.data?.author ?: "..."
                }
                is Status.Loading -> {
                    mViewBinding.tvQuote.text = getString(R.string.toast_msg_loading)
                    mViewBinding.tvAuthor.text = "..."
                }
                else -> {
                    mViewBinding.tvQuote.text = getString(R.string.toast_msg_loading)
                    mViewBinding.tvAuthor.text = "..."
                }
            }
        })
    }

    private fun getQuoteObserver() {
        mViewModel.quote.observe(this, { state ->
            when (state) {
                is Status.Error -> {
                    showToast(state.message)
                }
                is Status.Success -> {
                    twitterToggle(false)
                    textCopiedState(false)
                    mViewBinding.tvQuote.text = state.data?.quoteContent ?: "loading"
                    mViewBinding.tvAuthor.text = state.data?.author ?: "..."
                }
                is Status.Loading -> {
                    mViewBinding.tvQuote.text = getString(R.string.toast_msg_loading)
                    mViewBinding.tvAuthor.text = "..."
                }
                else -> {
                    mViewBinding.tvQuote.text = getString(R.string.toast_msg_loading)
                    mViewBinding.tvAuthor.text = "..."
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
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

            R.id.saved_item_icon -> {
                startActivity(Intent(this, AllQuotesActivity::class.java))
                true
            }

            else -> true
        }
    }

    private fun textCopiedState(state: Boolean) {
        if (state)
            mViewBinding.clipBoardImageView.setImageResource(R.drawable.ic_copy_selected)
        else
            mViewBinding.clipBoardImageView.setImageResource(R.drawable.ic_copy_unselected)
    }

    private fun networkCheck() {
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this) { isConnected ->
            if (!isConnected) {
                mViewBinding.textViewNetworkStatus.text =
                    getString(R.string.network_status_no_connections)
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
                if (mViewModel.randomQuote.value is Status.Error) {
                    loadRandomQuoteByDefault()
                    mViewBinding.btnToggleGroup.check(R.id.btn_random)
                }

                mViewBinding.textViewNetworkStatus.text = getString(R.string.network_status_online)
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

    fun View.show() {
        visibility = View.VISIBLE
    }

    fun View.hide() {
        visibility = View.GONE
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}