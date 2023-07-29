package com.elian.basictimer

data class TimerState(
	val hasStarted: Boolean = false,
	val isRunning: Boolean = false,
	val direction: TimerDirection = TimerDirection.COUNT_UP,
)

enum class TimerDirection {
	COUNT_UP,
	COUNT_DOWN,
}