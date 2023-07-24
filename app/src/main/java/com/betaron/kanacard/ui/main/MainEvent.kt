package com.betaron.kanacard.ui.main

sealed class MainEvent {
    data class SwitchAlphabet(val alphabetIndex: Int) : MainEvent()
    data class EnteredAnswer(val text: String) : MainEvent()
    data class SwitchSymbol(val checked: Boolean, val index: Int) : MainEvent()
    data class SwitchImeVisibility(val state: Boolean) : MainEvent()
    object SkipSymbol : MainEvent()
    object CheckAnswer : MainEvent()
    object SaveSelected : MainEvent()
}
