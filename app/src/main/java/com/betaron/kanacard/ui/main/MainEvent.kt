package com.betaron.kanacard.ui.main

sealed class MainEvent {
    data class SwitchAlphabet(val alphabetIndex: Int) : MainEvent()
    object PickNewSymbol : MainEvent()
}
