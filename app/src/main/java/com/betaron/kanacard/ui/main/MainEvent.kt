package com.betaron.kanacard.ui.main

import android.content.Context

sealed class MainEvent {
    data class SwitchAlphabet(val alphabetIndex: Int) : MainEvent()
    data class EnteredAnswer(val text: String, val context: Context) : MainEvent()
    object SkipSymbol : MainEvent()
    object CheckAnswer : MainEvent()
}
