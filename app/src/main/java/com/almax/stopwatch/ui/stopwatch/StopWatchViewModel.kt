package com.almax.stopwatch.ui.stopwatch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class StopWatchViewModel(
    private val repository: StopWatchRepository
) : ViewModel() {

    private val _timer = MutableStateFlow(0L)
    val timer: StateFlow<Long>
        get() = _timer

    private var isRunning: Boolean = false

    private var isServiceBound: Boolean = false

    init {
        repository.bindService()
        viewModelScope.launch {
            repository.isServiceBound.collect {
                isServiceBound = it
                if (!isServiceBound) {
                    isRunning = false
                    resetTimer()
                }
            }
        }
    }

    fun startTimer() {
        isRunning = true
        viewModelScope.launch {
            while (isServiceBound && isRunning) {
                repository.startTimer()
                    .catch { e ->
                        Log.e(TAG, "startTimer: ${e.message}")
                        isRunning = false
                    }
                    .collect { elapsedTime ->
                        _timer.value = elapsedTime
                    }
            }

        }
    }

    fun stopTimer() {
        isRunning = false
        repository.stopTimer()
    }

    fun resetTimer() {
        isRunning = false
        repository.resetTimer()
    }

    //TODO: does not get cleared!! check
    /*override fun onCleared() {
        super.onCleared()
        *//*if (!isServiceBound || !is)*//*
        repository.unbindService()
    }*/
}

const val TAG = "StopWatchViewModelTAG"