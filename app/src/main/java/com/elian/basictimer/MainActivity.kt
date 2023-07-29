package com.elian.basictimer

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.elian.basictimer.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private val viewModel by viewModels<MainViewModel>()


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		subscribeToEvents()
		initUi()
	}

	private fun initUi() {
		binding.apply {
			btnStart.setOnClickListener {
				viewModel.onAction(MainAction.Start)
			}
			btnPauseOrResume.setOnClickListener {
				viewModel.onAction(MainAction.PauseOrResume)
			}
			btnRestart.setOnClickListener {
				viewModel.onAction(MainAction.Restart)
			}
			btnUpOrDown.setOnClickListener {
				viewModel.onAction(MainAction.ToggleTimerDirection)
			}
		}
	}

	private fun subscribeToEvents() {

		lifecycleScope.launch {
			viewModel.millisState.flowWithLifecycle(lifecycle)
				.collectLatest { millis ->

					val timeFormatted = millis.absoluteValue.milliseconds.toComponents { hours, minutes, seconds, nanoseconds ->
						val milliseconds = nanoseconds.nanoseconds.inWholeMilliseconds

						val hoursFormatted = "%02d".format(hours)
						val minutesFormatted = "%02d".format(minutes)
						val secondsFormatted = "%02d".format(seconds)
						val millisecondsFormatted = "%03d".format(milliseconds)

						val sign = if (millis < 0) "-" else ""

						"$sign$hoursFormatted:$minutesFormatted:$secondsFormatted.$millisecondsFormatted"
					}

					binding.tvTime.text = timeFormatted
				}
		}
		lifecycleScope.launch {
			viewModel.timerState.flowWithLifecycle(lifecycle)
				.collectLatest { state ->

					val hasStarted = state.hasStarted
					val isRunning = state.isRunning
					val direction = state.direction

					binding.apply {

						btnStart.apply {
							isEnabled = !hasStarted
						}
						btnPauseOrResume.apply {
							text = when {
								isRunning -> "Pause"
								else      -> "Resume"
							}
							isEnabled = hasStarted
						}
						btnRestart.apply {
							isEnabled = hasStarted
						}

						btnUpOrDown.apply {
							text = when (direction) {
								TimerDirection.COUNT_UP   -> "Count down"
								TimerDirection.COUNT_DOWN -> "Count up"
							}
							setBackgroundTint(
								context.getColorCompat(
									when (direction) {
										TimerDirection.COUNT_UP   -> R.color.red_a200
										TimerDirection.COUNT_DOWN -> R.color.green_a200
									}
								)
							)
						}
					}
				}
		}
	}
}