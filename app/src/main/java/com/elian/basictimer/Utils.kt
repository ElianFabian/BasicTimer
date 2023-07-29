package com.elian.basictimer

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat


@ColorInt
fun Context.getColorCompat(@ColorRes colorId: Int): Int = ContextCompat.getColor(this, colorId)

fun View.setBackgroundTint(@ColorInt color: Int) {
	backgroundTintList = ColorStateList.valueOf(color)
}