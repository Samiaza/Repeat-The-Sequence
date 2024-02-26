package com.example.unrepeatability.util

import com.example.unrepeatability.R

enum class SoundTheme(val sounds: Array<Int>){
    ANIMALS(arrayOf(R.raw.any_drum1, R.raw.any_drum2, R.raw.any_drum3, R.raw.any_horse)),
    NOTES(arrayOf(R.raw.nota_do, R.raw.nota_fa, R.raw.nota_re, R.raw.nota_lya)),
    TECH(arrayOf(R.raw.mobile_icq, R.raw.mobile_iphone, R.raw.mobile_short, R.raw.mobile_sms))
}

val soundThemesMap = mapOf("animal" to SoundTheme.ANIMALS,
    "note" to SoundTheme.NOTES,
    "tech" to SoundTheme.TECH)