package com.almax.stopwatch.ui.stopwatch

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.almax.stopwatch.R
import com.almax.stopwatch.databinding.ActivityStopWatchBinding
import com.almax.stopwatch.utils.formatTime
import kotlinx.coroutines.launch

class StopWatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStopWatchBinding
    private val viewModel: StopWatchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStopWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
    }

    private fun setupUi() {
        binding.apply {
            btnTimer.setOnClickListener {
                viewModel.updateTimer()
            }
            btnReset.setOnClickListener {
                viewModel.resetTimer()
            }
        }
        observeDataAndUpdateUi()
    }

    private fun observeDataAndUpdateUi() {
        //TODO are two launch blocks needed? test this scenario
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isRunning.collect { isRunning ->
                    binding.btnTimer.text = if (isRunning) {
                        resources.getString(R.string.stop)
                    } else {
                        resources.getString(R.string.start)
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.timer.collect { time ->
                    binding.tvTimer.text = formatTime(time)
                }
            }
        }
    }
}