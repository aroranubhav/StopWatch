package com.almax.stopwatch

import android.app.Application
import com.almax.stopwatch.di.component.ApplicationComponent
import com.almax.stopwatch.di.component.DaggerApplicationComponent
import com.almax.stopwatch.di.module.ApplicationModule

class StopWatchApplication : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }

    private fun injectDependencies() {
        component = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this@StopWatchApplication))
            .build()
        component.inject(this@StopWatchApplication)
    }
}