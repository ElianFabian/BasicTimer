package com.elian.basictimer

sealed interface MainAction {
	object Start : MainAction
	object PauseOrResume : MainAction
	object Restart : MainAction
	object ToggleTimerDirection : MainAction
}