package com.almax.stopwatch.di

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationContext

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
annotation class ActivityContext