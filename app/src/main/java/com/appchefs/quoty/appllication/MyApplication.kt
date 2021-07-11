package com.appchefs.quoty.appllication

import android.app.Application
import com.appchefs.quoty.data.remote.NetworkService
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Singleton

@HiltAndroidApp
class MyApplication : Application() {

}