package com.example.android.recordme.utils

import android.widget.ImageButton
import com.example.android.recordme.R

fun makePauseButton(button: ImageButton) {
    button.setBackgroundResource(R.drawable.gray_button)
    button.setImageResource(R.drawable.ic_baseline_pause_48)
}

fun makeRecButton(button: ImageButton) {
    button.setBackgroundResource(R.drawable.rec_button)
    button.setImageResource(0)
}