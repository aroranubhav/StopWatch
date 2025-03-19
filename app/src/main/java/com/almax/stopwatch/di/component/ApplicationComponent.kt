package com.almax.stopwatch.di.component

import android.content.Context
import com.almax.stopwatch.StopWatchApplication
import com.almax.stopwatch.di.ApplicationContext
import com.almax.stopwatch.di.module.ApplicationModule
import dagger.Component

@Component(
    modules = [ApplicationModule::class]
)
interface ApplicationComponent {

    fun inject(application: StopWatchApplication)

    @ApplicationContext
    fun getContext(): Context
}