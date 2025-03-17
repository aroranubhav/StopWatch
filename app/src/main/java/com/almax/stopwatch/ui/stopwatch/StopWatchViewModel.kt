package com.almax.stopwatch.ui.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StopWatchViewModel : ViewModel() {

    private val _timer = MutableStateFlow(0L)
    val timer: StateFlow<Long>
        get() = _timer

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> get() = _isRunning

    private var startTime = 0L

    fun updateTimer() {
        if (!_isRunning.value) {
            _isRunning.value = true
            startTime = System.currentTimeMillis() - _timer.value
            viewModelScope.launch(Dispatchers.Default) {
                while (_isRunning.value) {
                    _timer.update {
                        System.currentTimeMillis() - startTime
                    }
                    delay(10L)
                }
            }
        } else {
            _isRunning.value = false
        }
    }

    fun resetTimer() {
        _timer.value = 0L
        _isRunning.value = false
    }
}