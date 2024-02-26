package com.example.unrepeatability.util

import com.example.unrepeatability.R

val gameButtonsIds = arrayOf(R.id.button1, R.id.button2, R.id.button3, R.id.button4)

val gameButtonIndexes = gameButtonsIds.mapIndexed { index, _ -> index }

val ButtonsDrawableIds = arrayOf(R.drawable.green_button, R.drawable.blue_button,
    R.drawable.red_button, R.drawable.purple_button)