package com.github.oelhirai.pour.ui

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.oelhirai.pour.R
import com.github.oelhirai.pour.data.viewmodel.PourViewModel
import com.github.oelhirai.pour.databinding.PourActivityBinding
import kotlinx.android.synthetic.main.pour_activity.*

class PourActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(PourViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: PourActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.pour_activity)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        setupTimers()
        setupReadyButton()
    }

    private fun setupTimers() {
        viewModel.currentPourPeriod.observe(this, Observer<Long> { pourPeriod ->
            chronometer_pour_countdown.apply {
                base = SystemClock.elapsedRealtime() + pourPeriod
                setOnChronometerTickListener {
                    if (SystemClock.elapsedRealtime() >= base) {
                        viewModel.setFinishedCurrentPour()
                        stop()
                    }
                }
                start()
            }
        })
        chronometer_timer.apply {
            base = SystemClock.elapsedRealtime()
            start()
        }
    }


    private fun setupReadyButton() {
        button_ready.setOnClickListener { viewModel.moveToNextPour() }
    }
}