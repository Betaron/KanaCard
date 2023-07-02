package com.betaron.kanacard.ui.main

sealed class MainEvent {
    data class SwitchAlphabet(val alphabetIndex: Int) : MainEvent()
    data class EnteredAnswer(val text: String) : MainEvent()
    object PickNewSymbol : MainEvent()
}
