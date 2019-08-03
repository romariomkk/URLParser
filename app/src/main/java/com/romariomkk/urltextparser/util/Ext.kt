package com.romariomkk.urltextparser.util

import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ProgressBar

fun ProgressBar.incProgress(maxProgressAction: () -> Unit) {
    this.progress = progress.plus(1)

    val percentage = Math.round((progress * 100 / max).toDouble())
    progressDrawable.setColorFilter(when {
        percentage <= 25 -> Color.RED
        percentage <= 50 -> Color.parseColor("#FF5722")
        percentage <= 99 -> Color.parseColor("#FFC107")
        else -> Color.GREEN
    }, PorterDuff.Mode.SRC_IN)

    if (progress == max) {
        maxProgressAction.invoke()
    }
}