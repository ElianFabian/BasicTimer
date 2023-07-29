package com.elian.basictimer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.basictimer.timer.SimpleTimer
import com.elian.basictimer.timer.Timer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel : ViewModel() {

	private val timer = SimpleTimer(
		direction = Timer.Direction.COUNT_UP,
		periodInMillis = 1L,
		initialMillis = 0L,
	)

	val timerState = timer.state.map { state ->
		TimerState(
			isRunning = state.isRunning,
			hasStarted = state.hasStarted,
			direction = when (state.direction) {
				Timer.Direction.COUNT_UP   -> TimerDirection.COUNT_UP
				Timer.Direction.COUNT_DOWN -> TimerDirection.COUNT_DOWN
			},
		)
	}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), TimerState())

	val millisState = timer.millisState


	fun onAction(action: MainAction) {
		when (action) {
			MainAction.Start                -> {
				playTimer()
			}
			MainAction.PauseOrResume        -> {
				val isRunning = timerState.value.isRunning

				when {
					isRunning -> pauseTimer()
					else      -> playTimer()
				}
			}
			MainAction.Restart              -> {
				restartTimer()
			}
			MainAction.ToggleTimerDirection -> {
				toggleDirection()
			}
		}
	}


	private fun playTimer() = timer.play()

	private fun pauseTimer() = timer.pause()

	private fun restartTimer() = timer.restart()

	private fun toggleDirection() {
		timer.direction = when (timer.direction) {
			Timer.Direction.COUNT_UP   -> Timer.Direction.COUNT_DOWN
			Timer.Direction.COUNT_DOWN -> Timer.Direction.COUNT_UP
		}
	}
}