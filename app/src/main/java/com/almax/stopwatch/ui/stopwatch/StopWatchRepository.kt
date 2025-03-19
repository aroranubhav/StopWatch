package com.almax.stopwatch.ui.stopwatch

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.almax.stopwatch.StopWatchService
import com.almax.stopwatch.di.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StopWatchRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private lateinit var timerService: StopWatchService

    private val _isServiceBound = MutableStateFlow(false)
    val isServiceBound: StateFlow<Boolean>
        get() = _isServiceBound

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val serviceBinder = binder as StopWatchService.StopWatchBinder
            timerService = serviceBinder.service
            _isServiceBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            _isServiceBound.value = false
        }
    }

    fun bindService() {
        if (!_isServiceBound.value) {
            val intent = Intent(context, StopWatchService::class.java)
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    fun startTimer(): Flow<Long> {
        return flow {
            timerService.startTimer().collect {
                emit(it)
            }
        }
    }

    fun stopTimer() {
        if (::timerService.isInitialized) {
            timerService.stopTimer()
        }
    }

    fun resetTimer() {
        if (::timerService.isInitialized) {
            timerService.resetTimer()
        }
    }

    fun unbindService() {
        if (_isServiceBound.value) {
            context.unbindService(connection)
        }
    }
}