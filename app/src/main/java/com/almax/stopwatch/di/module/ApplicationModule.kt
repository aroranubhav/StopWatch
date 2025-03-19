package com.almax.stopwatch.di.module

import android.app.Application
import android.content.Context
import com.almax.stopwatch.di.ApplicationContext
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(
    private val application: Application
) {

    @ApplicationContext
    @Provides
    fun provideContext(): Context =
        application
}