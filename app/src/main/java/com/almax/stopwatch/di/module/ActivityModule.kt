package com.almax.stopwatch.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.almax.stopwatch.di.ActivityContext
import com.almax.stopwatch.ui.base.ViewModelProviderFactory
import com.almax.stopwatch.ui.stopwatch.StopWatchRepository
import com.almax.stopwatch.ui.stopwatch.StopWatchViewModel
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(
    private val activity: AppCompatActivity
) {

    @ActivityContext
    @Provides
    fun provideContext(): Context =
        activity

    @Provides
    fun provideViewModel(
        repository: StopWatchRepository
    ): StopWatchViewModel {
        return ViewModelProvider(
            activity,
            ViewModelProviderFactory(StopWatchViewModel::class) {
                StopWatchViewModel(repository)
            })[StopWatchViewModel::class]
    }
}