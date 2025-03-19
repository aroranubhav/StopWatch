package com.almax.stopwatch.di.component

import com.almax.stopwatch.di.module.ActivityModule
import com.almax.stopwatch.ui.stopwatch.StopWatchActivity
import dagger.Component

@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {

    fun inject(activity: StopWatchActivity)
}