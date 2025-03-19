package com.almax.stopwatch.ui.stopwatch

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.almax.stopwatch.R
import com.almax.stopwatch.StopWatchApplication
import com.almax.stopwatch.databinding.ActivityStopWatchBinding
import com.almax.stopwatch.di.component.ActivityComponent
import com.almax.stopwatch.di.component.DaggerActivityComponent
import com.almax.stopwatch.di.module.ActivityModule
import com.almax.stopwatch.utils.formatTime
import kotlinx.coroutines.launch
import javax.inject.Inject

class StopWatchActivity : AppCompatActivity() {

    private lateinit var component: ActivityComponent
    private lateinit var binding: ActivityStopWatchBinding

    @Inject
    lateinit var viewModel: StopWatchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        binding = ActivityStopWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
    }

    private fun setupUi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS), 200
            )
        }

        binding.apply {
            btnTimer.setOnClickListener {
                updateTimer()
            }
            btnReset.setOnClickListener {
                resetTimer()
            }
        }
    }

    private fun updateTimer() {
        val timerBtnTest = binding.btnTimer.text

        if (timerBtnTest == resources.getString(R.string.start)) {
            viewModel.startTimer()

            lifecycleScope.launch {
                binding.btnTimer.text = resources.getString(R.string.stop)
                viewModel.timer.collect { elapsedTime ->
                    binding.tvTimer.text = formatTime(elapsedTime)
                }
            }
        } else {
            stopTimer()
        }
    }

    private fun resetTimer() {
        viewModel.resetTimer()
        binding.btnTimer.text = resources.getString(R.string.start)
        binding.tvTimer.text = "00:00:00"
    }

    private fun stopTimer() {
        viewModel.stopTimer()
        binding.btnTimer.text = resources.getString(R.string.start)
    }


    private fun injectDependencies() {
        component = DaggerActivityComponent
            .builder()
            .applicationComponent((application as StopWatchApplication).component)
            .activityModule(ActivityModule(this@StopWatchActivity))
            .build()
        component.inject(this@StopWatchActivity)
    }
}

const val MainActivityTAG = "MainActivityTAG"